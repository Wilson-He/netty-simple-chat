CREATE SCHEMA simple_chat
    AUTHORIZATION kuka;

CREATE TABLE public.chat_message
(
    id serial NOT NULL,
    sender_id character varying(32) NOT NULL,
    receiver_id character varying(32) NOT NULL,
    content text COLLATE pg_catalog."default",
    CONSTRAINT chat_message_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE simple_chat.chat_message
    OWNER to kuka;