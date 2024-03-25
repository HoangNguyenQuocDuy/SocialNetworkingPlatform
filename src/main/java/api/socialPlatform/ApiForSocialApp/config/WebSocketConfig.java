package api.socialPlatform.ApiForSocialApp.config;

        import com.auth0.jwt.JWT;
        import com.auth0.jwt.JWTVerifier;
        import com.auth0.jwt.algorithms.Algorithm;
        import com.auth0.jwt.interfaces.DecodedJWT;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.messaging.Message;
        import org.springframework.messaging.MessageChannel;
        import org.springframework.messaging.simp.config.ChannelRegistration;
        import org.springframework.messaging.simp.config.MessageBrokerRegistry;
        import org.springframework.messaging.simp.stomp.StompCommand;
        import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
        import org.springframework.messaging.support.ChannelInterceptor;
        import org.springframework.messaging.support.MessageHeaderAccessor;
        import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
        import org.springframework.security.core.userdetails.UserDetails;
        import org.springframework.security.core.userdetails.UserDetailsService;
        import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
        import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
        import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

        import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/rooms");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
    @Value("${secret.key}")
    private String secretKey;
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                logger.info(String.valueOf(accessor));
                logger.debug("Authorization: {}", accessor.getNativeHeader("Authorization"));
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    List<String> authorization = accessor.getNativeHeader("Authorization");
                    logger.debug("Authorization: {}", authorization);

                    String token = authorization.get(0).split(" ")[1];
                    if (token != null) {
                        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
                        JWTVerifier verifier = JWT.require(algorithm).build();
                        DecodedJWT decodedJWT = verifier.verify(token);
                        String username = decodedJWT.getSubject();
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );

                        accessor.setUser(authenticationToken);
                    }
                }

                return message;
            }
        });
    }
}
