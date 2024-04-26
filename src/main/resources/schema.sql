CREATE TABLE IF NOT EXISTS employees
(
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(50),
    surname         VARCHAR(50),
    passport_number VARCHAR(6),
    passport_date   DATE,
    salary          INTEGER
);

CREATE TABLE IF NOT EXISTS departments
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(100),
    city         VARCHAR(100),
    street       VARCHAR(100),
    region       VARCHAR(50),
    postal_code  VARCHAR(6),
    phone_number VARCHAR(16)
);

CREATE TABLE IF NOT EXISTS employees_departments
(
    employee_id   BIGINT,
    department_id INTEGER,
    PRIMARY KEY (employee_id, department_id)
);

ALTER TABLE employees_departments
    ADD CONSTRAINT fk_employee_id
        FOREIGN KEY (employee_id)
            REFERENCES employees (id),
    ADD CONSTRAINT fk_department_id
        FOREIGN KEY (department_id)
            REFERENCES departments (id);