CREATE TABLE IF NOT EXISTS st_token_pushnotification
(
  id bigint NOT NULL,
  userid character varying(16) NOT NULL,
  token character varying(256) NOT NULL,
  savedate timestamp without time zone NOT NULL  
);
