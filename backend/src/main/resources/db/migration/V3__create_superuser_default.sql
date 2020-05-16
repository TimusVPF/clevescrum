-- This is the 1st Account. SuperAdmin/SuperAdmin
INSERT INTO users(id, username, email, password, enabled) VALUES (1, 'SuperAdmin', 'cleveland@clevecord.me', '$2a$12$fq0qegxp8kcCLs5uX/tEW.ulAjyDyFImWD5Ey.NKbuO9T2s0tGiSK', true);

INSERT INTO user_roles (user_id, role) VALUES (1, 'ROLE_USER');
INSERT INTO user_roles (user_id, role) VALUES (1, 'ROLE_MODERATOR');
INSERT INTO user_roles (user_id, role) VALUES (1, 'ROLE_ADMIN');
INSERT INTO user_roles (user_id, role) VALUES (1, 'ROLE_SUPERADMIN');

-- Do this because superadmin is inserted with id = 1.
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users) + 1);
