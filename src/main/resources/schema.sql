ALTER TABLE userx_user_role
    DROP CONSTRAINT IF EXISTS Username_Role;
ALTER TABLE userx_user_role
    ADD CONSTRAINT Username_Role UNIQUE (userx_username,roles);