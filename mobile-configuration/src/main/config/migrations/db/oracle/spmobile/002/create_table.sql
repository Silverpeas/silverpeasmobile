CREATE TABLE st_token_pushnotification
(
  id bigint NOT NULL,
  userid VARCHAR(16) NOT NULL,
  token VARCHAR(256) NOT NULL,
  savedate TIMESTAMP NOT NULL  
);