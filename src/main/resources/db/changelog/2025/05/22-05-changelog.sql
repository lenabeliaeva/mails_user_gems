-- liquibase formatted sql

-- changeset ebeliaeva:1747930594568-1
CREATE SEQUENCE company_seq START WITH 1 INCREMENT BY 50;

-- changeset ebeliaeva:1747930594568-2
CREATE SEQUENCE email_seq START WITH 1 INCREMENT BY 50;

-- changeset ebeliaeva:1747930594568-3
CREATE SEQUENCE event_fetch_state_seq START WITH 1 INCREMENT BY 50;

-- changeset ebeliaeva:1747930594568-4
CREATE SEQUENCE event_seq START WITH 1 INCREMENT BY 50;

-- changeset ebeliaeva:1747930594568-5
CREATE SEQUENCE person_seq START WITH 1 INCREMENT BY 50;

-- changeset ebeliaeva:1747930594568-6
CREATE TABLE company
(
    id           BIGINT NOT NULL,
    name         VARCHAR(255),
    linkedin_url VARCHAR(255),
    employees    INT    NOT NULL,
    CONSTRAINT pk_company PRIMARY KEY (id)
);

-- changeset ebeliaeva:1747930594568-7
CREATE TABLE email
(
    id            BIGINT NOT NULL,
    sent_at       TIMESTAMP,
    email_content VARCHAR(255),
    recipient_id  BIGINT,
    event_id      BIGINT,
    CONSTRAINT pk_email PRIMARY KEY (id)
);

-- changeset ebeliaeva:1747930594568-8
CREATE TABLE event
(
    id       BIGINT NOT NULL,
    title    VARCHAR(255),
    start    TIMESTAMP,
    "end"    TIMESTAMP,
    changed  TIMESTAMP,
    event_id BIGINT,
    CONSTRAINT pk_event PRIMARY KEY (id)
);

-- changeset ebeliaeva:1747930594568-9
CREATE TABLE event_fetch_state
(
    id                      BIGINT NOT NULL,
    last_fetched_event_date TIMESTAMP,
    updated_at              TIMESTAMP,
    person_id               BIGINT,
    CONSTRAINT pk_eventfetchstate PRIMARY KEY (id)
);

-- changeset ebeliaeva:1747930594568-10
CREATE TABLE event_person
(
    response_status VARCHAR(255),
    event_id        BIGINT NOT NULL,
    person_id       BIGINT NOT NULL,
    CONSTRAINT pk_eventperson PRIMARY KEY (event_id, person_id)
);

-- changeset ebeliaeva:1747930594568-11
CREATE TABLE person
(
    id           BIGINT NOT NULL,
    first_name   VARCHAR(255),
    last_name    VARCHAR(255),
    email        VARCHAR(255),
    avatar_link  VARCHAR(255),
    title        VARCHAR(255),
    linkedin_url VARCHAR(255),
    last_updated TIMESTAMP,
    company_id   BIGINT,
    CONSTRAINT pk_person PRIMARY KEY (id)
);

-- changeset ebeliaeva:1747930594568-12
ALTER TABLE email
    ADD CONSTRAINT FK_EMAIL_ON_EVENT FOREIGN KEY (event_id) REFERENCES event (id);

-- changeset ebeliaeva:1747930594568-13
ALTER TABLE email
    ADD CONSTRAINT FK_EMAIL_ON_RECIPIENT FOREIGN KEY (recipient_id) REFERENCES person (id);

-- changeset ebeliaeva:1747930594568-14
ALTER TABLE event_fetch_state
    ADD CONSTRAINT FK_EVENTFETCHSTATE_ON_PERSON FOREIGN KEY (person_id) REFERENCES person (id);

-- changeset ebeliaeva:1747930594568-15
ALTER TABLE event_person
    ADD CONSTRAINT FK_EVENTPERSON_ON_EVENT FOREIGN KEY (event_id) REFERENCES event (id);

-- changeset ebeliaeva:1747930594568-16
ALTER TABLE event_person
    ADD CONSTRAINT FK_EVENTPERSON_ON_PERSON FOREIGN KEY (person_id) REFERENCES person (id);

-- changeset ebeliaeva:1747930594568-17
ALTER TABLE person
    ADD CONSTRAINT FK_PERSON_ON_COMPANY FOREIGN KEY (company_id) REFERENCES company (id);

