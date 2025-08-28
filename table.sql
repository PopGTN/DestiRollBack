CREATE TABLE IF NOT EXISTS DestiRollBack
(
    id             INT PRIMARY KEY AUTO_INCREMENT,
    playersUUID    VARCHAR(255),
    backUpDate     VARCHAR(255),
    eventType      VARCHAR(255),
    worldUUID      VARCHAR(255),
    playersXP      FLOAT,
    eventTypeCause VARCHAR(255),
    inventory      LONGTEXT
);
