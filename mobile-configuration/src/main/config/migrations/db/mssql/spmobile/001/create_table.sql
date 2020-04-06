

CREATE TABLE SC_Gallery_OrderDetail (
  orderId          VARCHAR(40) NOT NULL,
  mediaId          VARCHAR(40) NOT NULL,
  instanceId       VARCHAR(50) NOT NULL,
  downloadDate     datetime   NULL,
  downloadDecision VARCHAR(50) NULL
);

CREATE TABLE st_token_pushnotification
(
  id bigint NOT NULL,
  userid VARCHAR(16) NOT NULL,
  token VARCHAR(256) NOT NULL,
  savedate datetime NOT NULL  
);
