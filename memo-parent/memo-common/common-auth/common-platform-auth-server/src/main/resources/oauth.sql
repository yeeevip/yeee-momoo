
CREATE TABLE oauth2_registered_client (
  id nvarchar(100) NOT NULL,
  client_id nvarchar(100) NOT NULL,
  client_id_issued_at datetime2(7) NOT NULL,
  client_secret nvarchar(200) DEFAULT (NULL),
  client_secret_expires_at datetime2(7) DEFAULT (NULL),
  client_name nvarchar(200) NOT NULL,
  client_authentication_methods nvarchar(1000) NOT NULL,
  authorization_grant_types nvarchar(1000) NOT NULL,
  redirect_uris nvarchar(1000) DEFAULT (NULL),
  post_logout_redirect_uris nvarchar(1000) DEFAULT (NULL),
  scopes nvarchar(1000) NOT NULL,
  client_settings nvarchar(2000) NOT NULL,
  token_settings nvarchar(2000) NOT NULL,
  CONSTRAINT PK__oauth2_r__3213E83F111100BE PRIMARY KEY (id)
)

INSERT INTO oauth2_registered_client
(id, client_id, client_id_issued_at, client_secret, client_secret_expires_at, client_name, client_authentication_methods, authorization_grant_types, redirect_uris, post_logout_redirect_uris, scopes, client_settings, token_settings)
VALUES('ff256e2a-b380-47c2-a879-77117ea65b40', 'BB254001', '2024-03-12 17:18:00.534', '$2a$10$X4FgJxBows9aJQRkkBwrd.uYERZm8wko5Ee95zRyDfyKLOPP/QquG', '2024-03-26 17:18:00.623', 'BB254001', 'client_secret_post,client_secret_basic', 'refresh_token,client_credentials', '', 'http://127.0.0.1:8081/logout', 'message.read', '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":false}', '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",7200],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",300.000000000]}');
