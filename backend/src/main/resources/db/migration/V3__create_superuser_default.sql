-- This is the 1st Account. SuperAdmin/SuperAdmin
INSERT INTO users(username, email, password, enabled) VALUES ('SuperAdmin', 'cleveland@clevecord.me', '$2y$12$5HbgAuQWpIlVLAgMECMZ5eZBcnE9T04oUh4UNPB83oi/ll7BR6y5.', true);

INSERT INTO user_roles (username, role) VALUES ('SuperAdmin', 'ROLE_USER');
INSERT INTO user_roles (username, role) VALUES ('SuperAdmin', 'ROLE_ADMIN');
