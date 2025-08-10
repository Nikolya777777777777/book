CREATE TABLE users_roles (
                       user_id BIGINT,
                       role_id BIGINT,
                       CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                       CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);
