CREATE TABLE sc_spmobil_loginevents (
    id BIGINT IDENTITY(1,1) NOT NULL,
    user_id BIGINT NOT NULL,
    occurred_at DATETIMEOFFSET NOT NULL,
    platform SMALLINT NOT NULL,       -- 1=ios, 2=android
    app_version NVARCHAR(255),
    os_version NVARCHAR(255),
    ip_address NVARCHAR(45),          -- pour IPv4 ou IPv6
    country_code CHAR(2),
    device NVARCHAR(45),

    CONSTRAINT PK_sc_spmobil_loginevents PRIMARY KEY (id, occurred_at)
);