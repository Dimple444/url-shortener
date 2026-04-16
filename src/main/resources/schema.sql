-- Create the sequence
CREATE SEQUENCE IF NOT EXISTS url_sequence
    START WITH 10000
    INCREMENT BY 1
    CACHE 50;

ALTER SEQUENCE url_sequence CACHE 5;