CREATE TABLE sc_spmobil_loginevents (
        id BIGSERIAL,
        user_id BIGINT NOT NULL,
        occurred_at TIMESTAMPTZ NOT NULL,
        platform SMALLINT NOT NULL,        -- 1=ios, 2=android
        app_version TEXT,
        os_version TEXT,
        ip_address VARCHAR(45),
        country_code CHAR(2),
        device VARCHAR(45),

        PRIMARY KEY (id, occurred_at)
);