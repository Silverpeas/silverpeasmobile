CREATE TABLE sc_spmobil_loginevents (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        user_id BIGINT NOT NULL,
        occurred_at TIMESTAMP NOT NULL,
        platform SMALLINT NOT NULL,        -- 1=ios, 2=android
        app_version VARCHAR(255),
        os_version VARCHAR(255),
        ip_address VARCHAR(45),            -- IPv4 ou IPv6
        country_code CHAR(2),
        device VARCHAR(45)
);