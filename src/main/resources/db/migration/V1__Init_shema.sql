CREATE TABLE payment (
    id VARCHAR(300) PRIMARY KEY,
    payment_date TIMESTAMP NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'VERIFYING'
);

CREATE TABLE donor (
   id BIGSERIAL PRIMARY KEY,
   email VARCHAR(300) NOT NULL,
   full_name VARCHAR(300) NOT NULL
);

CREATE TABLE beneficiary (
   id BIGSERIAL PRIMARY KEY,
   email VARCHAR(300) NOT NULL,
   full_name VARCHAR(300) NOT NULL
);

CREATE TABLE donation (
    id BIGSERIAL PRIMARY KEY,
    donor_id BIGINT NOT NULL,
    payment_id VARCHAR(300) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (donor_id) REFERENCES donor(id),
    FOREIGN KEY (payment_id) REFERENCES payment(id)
);

CREATE TABLE help (
    id BIGSERIAL PRIMARY KEY,
    beneficiary_id BIGINT NOT NULL,
    payment_id VARCHAR(300) NOT NULL,
    accident_description TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (beneficiary_id) REFERENCES beneficiary(id),
    FOREIGN KEY (payment_id) REFERENCES payment(id)
);

CREATE INDEX idx_donation_created_at ON donation(created_at DESC);
CREATE INDEX idx_help_created_at ON help(created_at DESC);
CREATE INDEX idx_payment_status ON payment(status);