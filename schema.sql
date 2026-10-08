-- ============================================================================
-- SafeRide Insurance Management System - Database Schema (MySQL 8.0+)
-- SE2030 Software Engineering - Group 2026-Y2-S1-MLB-WEB2G1-09
-- ============================================================================

CREATE DATABASE IF NOT EXISTS saferide_db;
USE saferide_db;

-- Disable Foreign Key Checks for clean table drop/re-creation
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS pre_policy_inspections;
DROP TABLE IF EXISTS damage_assessments;
DROP TABLE IF EXISTS inspections;
DROP TABLE IF EXISTS accident_claims;
DROP TABLE IF EXISTS theft_claims;
DROP TABLE IF EXISTS windscreen_claims;
DROP TABLE IF EXISTS claims;
DROP TABLE IF EXISTS standard_quotes;
DROP TABLE IF EXISTS discounted_quotes;
DROP TABLE IF EXISTS premium_quotes;
DROP TABLE IF EXISTS comprehensive_policies;
DROP TABLE IF EXISTS third_party_policies;
DROP TABLE IF EXISTS policies;
DROP TABLE IF EXISTS private_cars;
DROP TABLE IF EXISTS commercial_vehicles;
DROP TABLE IF EXISTS motorcycles;
DROP TABLE IF EXISTS vehicles;
DROP TABLE IF EXISTS customer_profiles;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS claims_officers;
DROP TABLE IF EXISTS insurance_agents;
DROP TABLE IF EXISTS admins;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------------------------------------------------------
-- 1. BASE USERS TABLE & ROLE-SPECIFIC SUBCLASSES (OOP Inheritance: JOINED)
-- ----------------------------------------------------------------------------
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    role VARCHAR(30) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE customers (
    id BIGINT PRIMARY KEY,
    preferred_communication VARCHAR(20) DEFAULT 'EMAIL',
    loyalty_points INT DEFAULT 0,
    CONSTRAINT fk_customer_user FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE claims_officers (
    id BIGINT PRIMARY KEY,
    employee_id VARCHAR(30) NOT NULL UNIQUE,
    department VARCHAR(50) NOT NULL,
    adjudication_limit DECIMAL(12,2) DEFAULT 500000.00,
    CONSTRAINT fk_officer_user FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE insurance_agents (
    id BIGINT PRIMARY KEY,
    agent_license_number VARCHAR(50) NOT NULL UNIQUE,
    agency_branch VARCHAR(50) NOT NULL,
    commission_rate DECIMAL(5,2) DEFAULT 5.00,
    CONSTRAINT fk_agent_user FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE admins (
    id BIGINT PRIMARY KEY,
    admin_level VARCHAR(30) NOT NULL DEFAULT 'SUPER_ADMIN',
    department VARCHAR(50) NOT NULL DEFAULT 'Executive',
    CONSTRAINT fk_admin_user FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 2. CUSTOMER PROFILE MANAGEMENT (Deshanya T. - IT25102295)
-- ----------------------------------------------------------------------------
CREATE TABLE customer_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    nic_passport VARCHAR(30) NOT NULL UNIQUE,
    date_of_birth DATE NOT NULL,
    driving_license_no VARCHAR(40) NOT NULL UNIQUE,
    license_expiry_date DATE NOT NULL,
    residential_address VARCHAR(255) NOT NULL,
    city VARCHAR(50) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    occupation VARCHAR(100),
    emergency_contact_name VARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_profile_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 3. VEHICLE MANAGEMENT (Ravihara L.D.C. - IT25102267)
-- ----------------------------------------------------------------------------
CREATE TABLE vehicles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    vehicle_type VARCHAR(30) NOT NULL,
    make VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    manufacture_year INT NOT NULL,
    registration_number VARCHAR(30) NOT NULL UNIQUE,
    chassis_number VARCHAR(50) NOT NULL UNIQUE,
    engine_capacity_cc INT NOT NULL,
    fuel_type VARCHAR(30) NOT NULL,
    market_value DECIMAL(12,2) NOT NULL,
    current_mileage INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_vehicle_customer FOREIGN KEY (customer_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE private_cars (
    id BIGINT PRIMARY KEY,
    seating_capacity INT NOT NULL DEFAULT 5,
    has_anti_theft_device BOOLEAN DEFAULT TRUE,
    body_type VARCHAR(30) DEFAULT 'SEDAN',
    CONSTRAINT fk_private_car FOREIGN KEY (id) REFERENCES vehicles(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE commercial_vehicles (
    id BIGINT PRIMARY KEY,
    tonnage_capacity DECIMAL(8,2) NOT NULL,
    goods_permit_number VARCHAR(50) NOT NULL,
    commercial_use_type VARCHAR(50) NOT NULL,
    CONSTRAINT fk_commercial_vehicle FOREIGN KEY (id) REFERENCES vehicles(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE motorcycles (
    id BIGINT PRIMARY KEY,
    has_helmet_box BOOLEAN DEFAULT FALSE,
    engine_stroke_type VARCHAR(20) DEFAULT 'FOUR_STROKE',
    CONSTRAINT fk_motorcycle FOREIGN KEY (id) REFERENCES vehicles(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 4. INSURANCE POLICY MANAGEMENT (Gamage S.M. - IT25102293)
-- ----------------------------------------------------------------------------
CREATE TABLE policies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    policy_number VARCHAR(50) NOT NULL UNIQUE,
    customer_id BIGINT NOT NULL,
    vehicle_id BIGINT NOT NULL,
    policy_type VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    sum_insured DECIMAL(12,2) NOT NULL,
    base_premium DECIMAL(12,2) NOT NULL,
    total_annual_premium DECIMAL(12,2) NOT NULL,
    deductible_amount DECIMAL(12,2) NOT NULL DEFAULT 5000.00,
    terms_agreed BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_policy_customer FOREIGN KEY (customer_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_policy_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE comprehensive_policies (
    id BIGINT PRIMARY KEY,
    includes_natural_disaster BOOLEAN DEFAULT TRUE,
    includes_theft BOOLEAN DEFAULT TRUE,
    includes_riot_strike BOOLEAN DEFAULT TRUE,
    roadside_assistance_level VARCHAR(30) DEFAULT 'PLATINUM',
    CONSTRAINT fk_comp_policy FOREIGN KEY (id) REFERENCES policies(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE third_party_policies (
    id BIGINT PRIMARY KEY,
    third_party_property_limit DECIMAL(12,2) NOT NULL DEFAULT 1000000.00,
    legal_liability_coverage BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_tp_policy FOREIGN KEY (id) REFERENCES policies(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 5. PREMIUM CALCULATION & QUOTE (Subasinghe S.A.K.G. - IT25102301)
-- ----------------------------------------------------------------------------
CREATE TABLE premium_quotes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quote_reference VARCHAR(50) NOT NULL UNIQUE,
    customer_id BIGINT NOT NULL,
    vehicle_id BIGINT,
    quote_type VARCHAR(30) NOT NULL,
    coverage_type VARCHAR(30) NOT NULL,
    calculated_sum_insured DECIMAL(12,2) NOT NULL,
    base_rate DECIMAL(6,4) NOT NULL,
    risk_loading_percent DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    ncb_discount_percent DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    final_premium DECIMAL(12,2) NOT NULL,
    is_applied_to_policy BOOLEAN NOT NULL DEFAULT FALSE,
    valid_until DATE NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_quote_customer FOREIGN KEY (customer_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_quote_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE standard_quotes (
    id BIGINT PRIMARY KEY,
    standard_processing_fee DECIMAL(10,2) DEFAULT 1500.00,
    CONSTRAINT fk_std_quote FOREIGN KEY (id) REFERENCES premium_quotes(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE discounted_quotes (
    id BIGINT PRIMARY KEY,
    promo_code VARCHAR(30),
    special_discount_rate DECIMAL(5,2) DEFAULT 0.00,
    loyalty_years INT DEFAULT 0,
    CONSTRAINT fk_disc_quote FOREIGN KEY (id) REFERENCES premium_quotes(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 6. ONLINE CLAIM MANAGEMENT (Arachchi A.M.R.S. - IT25102341 - Lead)
-- ----------------------------------------------------------------------------
CREATE TABLE claims (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    claim_reference VARCHAR(50) NOT NULL UNIQUE,
    policy_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    claim_type VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'SUBMITTED',
    incident_date DATE NOT NULL,
    incident_location VARCHAR(150) NOT NULL,
    incident_description TEXT NOT NULL,
    estimated_damage_amount DECIMAL(12,2) NOT NULL,
    approved_payout_amount DECIMAL(12,2) DEFAULT 0.00,
    assessor_notes TEXT,
    rejection_reason TEXT,
    settlement_date DATE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_claim_policy FOREIGN KEY (policy_id) REFERENCES policies(id) ON DELETE RESTRICT,
    CONSTRAINT fk_claim_customer FOREIGN KEY (customer_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE accident_claims (
    id BIGINT PRIMARY KEY,
    third_party_vehicle_involved BOOLEAN DEFAULT FALSE,
    police_report_number VARCHAR(50),
    driver_name_at_incident VARCHAR(100) NOT NULL,
    collision_type VARCHAR(50) NOT NULL,
    CONSTRAINT fk_acc_claim FOREIGN KEY (id) REFERENCES claims(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE theft_claims (
    id BIGINT PRIMARY KEY,
    police_station_reported VARCHAR(100) NOT NULL,
    crime_reference_number VARCHAR(50) NOT NULL,
    recovery_status VARCHAR(30) DEFAULT 'NOT_RECOVERED',
    last_known_location VARCHAR(150) NOT NULL,
    CONSTRAINT fk_thf_claim FOREIGN KEY (id) REFERENCES claims(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE windscreen_claims (
    id BIGINT PRIMARY KEY,
    glass_type VARCHAR(30) NOT NULL DEFAULT 'FRONT_WINDSCREEN',
    repair_workshop_name VARCHAR(100) NOT NULL,
    is_replacement BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_wsc_claim FOREIGN KEY (id) REFERENCES claims(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 7. VEHICLE INSPECTION & DAMAGE ASSESSMENT (Senavirathna M.D.T.D. - IT25102348)
-- ----------------------------------------------------------------------------
CREATE TABLE inspections (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    inspection_reference VARCHAR(50) NOT NULL UNIQUE,
    vehicle_id BIGINT NOT NULL,
    claim_id BIGINT,
    inspector_id BIGINT,
    inspection_type VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    inspection_date DATE NOT NULL,
    inspection_location VARCHAR(150) NOT NULL,
    physical_condition_notes TEXT,
    damage_severity VARCHAR(30) DEFAULT 'MODERATE',
    estimated_repair_cost DECIMAL(12,2) DEFAULT 0.00,
    is_passed BOOLEAN DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_insp_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE,
    CONSTRAINT fk_insp_claim FOREIGN KEY (claim_id) REFERENCES claims(id) ON DELETE SET NULL,
    CONSTRAINT fk_insp_inspector FOREIGN KEY (inspector_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE pre_policy_inspections (
    id BIGINT PRIMARY KEY,
    odometer_verified BOOLEAN DEFAULT TRUE,
    body_condition_rating INT DEFAULT 8,
    mechanical_score INT DEFAULT 9,
    CONSTRAINT fk_pre_policy_insp FOREIGN KEY (id) REFERENCES inspections(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE damage_assessments (
    id BIGINT PRIMARY KEY,
    structural_damage_found BOOLEAN DEFAULT FALSE,
    parts_to_replace TEXT,
    labor_cost_estimate DECIMAL(12,2) DEFAULT 0.00,
    recommended_action VARCHAR(50) DEFAULT 'REPAIR_AUTHORIZED',
    CONSTRAINT fk_damage_assess FOREIGN KEY (id) REFERENCES inspections(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ============================================================================
-- SEED DATA FOR DEMO & TESTING
-- ============================================================================

-- Users
INSERT INTO users (id, username, password, email, full_name, phone, role, is_active) VALUES
(1, 'admin', 'admin123', 'admin@saferide.com', 'Ranil Wickramasinghe (Admin)', '+94771110001', 'ROLE_ADMIN', 1),
(2, 'agent_kamal', 'agent123', 'kamal.agent@saferide.com', 'Kamal Perera (Agent)', '+94772220002', 'ROLE_INSURANCE_AGENT', 1),
(3, 'officer_nimal', 'officer123', 'nimal.claims@saferide.com', 'Nimal Silva (Claims Officer)', '+94773330003', 'ROLE_CLAIMS_OFFICER', 1),
(4, 'customer_sunil', 'customer123', 'sunil.fernando@gmail.com', 'Sunil Fernando', '+94774440004', 'ROLE_CUSTOMER', 1),
(5, 'customer_anusha', 'customer123', 'anusha.de.silva@gmail.com', 'Anusha De Silva', '+94775550005', 'ROLE_CUSTOMER', 1);

INSERT INTO admins (id, admin_level, department) VALUES (1, 'SUPER_ADMIN', 'Executive IT Operations');
INSERT INTO insurance_agents (id, agent_license_number, agency_branch, commission_rate) VALUES (2, 'AG-LK-2026-9901', 'Colombo Central Branch', 6.50);
INSERT INTO claims_officers (id, employee_id, department, adjudication_limit) VALUES (3, 'EMP-CLM-4402', 'Motor Claims Assessment', 1000000.00);
INSERT INTO customers (id, preferred_communication, loyalty_points) VALUES 
(4, 'EMAIL', 150),
(5, 'SMS', 200);

-- Customer Profiles
INSERT INTO customer_profiles (user_id, nic_passport, date_of_birth, driving_license_no, license_expiry_date, residential_address, city, postal_code, occupation, emergency_contact_name, emergency_contact_phone) VALUES
(4, '198812304567', '1988-05-14', 'B8849201', '2028-05-14', '45/2 Temple Road', 'Colombo 07', '00700', 'Senior Software Engineer', 'Chathuri Fernando', '+94778889901'),
(5, '199267891234', '1992-09-22', 'B9210982', '2030-09-22', '128 Galle Road', 'Mount Lavinia', '10370', 'Marketing Consultant', 'Rohan De Silva', '+94778889902');

-- Vehicles
INSERT INTO vehicles (id, customer_id, vehicle_type, make, model, manufacture_year, registration_number, chassis_number, engine_capacity_cc, fuel_type, market_value, current_mileage, is_active) VALUES
(1, 4, 'PRIVATE_CAR', 'Toyota', 'Prius Hybrid', 2021, 'WP-CAB-4521', 'JTDKN36U801928374', 1800, 'HYBRID', 12500000.00, 32000, 1),
(2, 4, 'MOTORCYCLE', 'Yamaha', 'FZ-S V3', 2022, 'WP-BFF-8841', 'ME4RG123891029384', 150, 'PETROL', 950000.00, 14500, 1),
(3, 5, 'COMMERCIAL_VEHICLE', 'Isuzu', 'Elf Light Truck', 2019, 'WP-ND-6019', 'ISZ98172648102938', 2990, 'DIESEL', 8200000.00, 85000, 1);

INSERT INTO private_cars (id, seating_capacity, has_anti_theft_device, body_type) VALUES (1, 5, 1, 'SEDAN');
INSERT INTO motorcycles (id, has_helmet_box, engine_stroke_type) VALUES (2, 1, 'FOUR_STROKE');
INSERT INTO commercial_vehicles (id, tonnage_capacity, goods_permit_number, commercial_use_type) VALUES (3, 3.50, 'GP-WP-88192', 'LOGISTICS_TRANSPORT');

-- Policies
INSERT INTO policies (id, policy_number, customer_id, vehicle_id, policy_type, status, start_date, end_date, sum_insured, base_premium, total_annual_premium, deductible_amount, terms_agreed) VALUES
(1, 'POL-2026-COMP-001', 4, 1, 'COMPREHENSIVE', 'ACTIVE', '2026-01-01', '2027-01-01', 12500000.00, 150000.00, 162500.00, 10000.00, 1),
(2, 'POL-2026-TP-002', 4, 2, 'THIRD_PARTY', 'ACTIVE', '2026-02-15', '2027-02-15', 950000.00, 18000.00, 21500.00, 2500.00, 1),
(3, 'POL-2026-COMP-003', 5, 3, 'COMPREHENSIVE', 'ACTIVE', '2026-03-10', '2027-03-10', 8200000.00, 120000.00, 134000.00, 15000.00, 1);

INSERT INTO comprehensive_policies (id, includes_natural_disaster, includes_theft, includes_riot_strike, roadside_assistance_level) VALUES
(1, 1, 1, 1, 'PLATINUM'),
(3, 1, 1, 0, 'GOLD');

INSERT INTO third_party_policies (id, third_party_property_limit, legal_liability_coverage) VALUES
(2, 500000.00, 1);

-- Premium Quotes
INSERT INTO premium_quotes (id, quote_reference, customer_id, vehicle_id, quote_type, coverage_type, calculated_sum_insured, base_rate, risk_loading_percent, ncb_discount_percent, final_premium, is_applied_to_policy, valid_until) VALUES
(1, 'QUO-2026-88192', 4, 1, 'STANDARD', 'COMPREHENSIVE', 12500000.00, 0.0120, 5.00, 20.00, 162500.00, 1, '2026-12-31'),
(2, 'QUO-2026-99041', 5, 3, 'DISCOUNTED', 'COMPREHENSIVE', 8200000.00, 0.0145, 10.00, 15.00, 134000.00, 1, '2026-12-31'),
(3, 'QUO-2026-10293', 4, 2, 'STANDARD', 'THIRD_PARTY', 950000.00, 0.0200, 0.00, 10.00, 21500.00, 1, '2026-12-31');

INSERT INTO standard_quotes (id, standard_processing_fee) VALUES (1, 1500.00), (3, 1000.00);
INSERT INTO discounted_quotes (id, promo_code, special_discount_rate, loyalty_years) VALUES (2, 'SAFEPROMO2026', 7.50, 3);

-- Claims
INSERT INTO claims (id, claim_reference, policy_id, customer_id, claim_type, status, incident_date, incident_location, incident_description, estimated_damage_amount, approved_payout_amount, assessor_notes, settlement_date) VALUES
(1, 'CLM-2026-ACC-001', 1, 4, 'ACCIDENT', 'UNDER_REVIEW', '2026-06-12', 'High Level Road, Nugegoda', 'Front bumper collided with roadside curb during heavy rain.', 85000.00, 75000.00, 'Assessor verified front bumper scratch and fog light bracket fracture. Payout adjusted.', NULL),
(2, 'CLM-2026-WSC-002', 1, 4, 'WINDSCREEN', 'APPROVED', '2026-07-04', 'Southern Expressway Km 32', 'Flying pebble caused severe crack on the front windshield.', 45000.00, 45000.00, 'Windscreen replacement authorized at authorized Toyota Center.', '2026-07-06'),
(3, 'CLM-2026-THF-003', 3, 5, 'THEFT', 'INSPECTION_SCHEDULED', '2026-08-01', 'Peliyagoda Market Yard', 'Spare wheel and side mirrors stolen while vehicle was parked overnight.', 110000.00, 0.00, 'Police extract obtained. Physical damage inspection booked.', NULL);

INSERT INTO accident_claims (id, third_party_vehicle_involved, police_report_number, driver_name_at_incident, collision_type) VALUES
(1, 0, 'POL-NUG-2026-881', 'Sunil Fernando', 'SINGLE_VEHICLE_OBSTACLE');

INSERT INTO windscreen_claims (id, glass_type, repair_workshop_name, is_replacement) VALUES
(2, 'FRONT_WINDSCREEN', 'Toyota Lanka Wattala', 1);

INSERT INTO theft_claims (id, police_station_reported, crime_reference_number, recovery_status, last_known_location) VALUES
(3, 'Peliyagoda Police Station', 'CR-PEL-2026-442', 'NOT_RECOVERED', 'Peliyagoda Commercial Zone');

-- Inspections
INSERT INTO inspections (id, inspection_reference, vehicle_id, claim_id, inspector_id, inspection_type, status, inspection_date, inspection_location, physical_condition_notes, damage_severity, estimated_repair_cost, is_passed) VALUES
(1, 'INSP-2026-PRE-001', 1, NULL, 3, 'PRE_POLICY', 'COMPLETED', '2025-12-28', 'Colombo Central Yard', 'Vehicle in pristine condition, genuine low mileage verified.', 'NONE', 0.00, 1),
(2, 'INSP-2026-DMG-002', 1, 1, 3, 'DAMAGE_ASSESSMENT', 'COMPLETED', '2026-06-14', 'Nugegoda Service Center', 'Front bumper cover cracked, lower radiator grille intact, sensors functioning.', 'MINOR', 85000.00, 1),
(3, 'INSP-2026-DMG-003', 3, 3, 3, 'DAMAGE_ASSESSMENT', 'PENDING', '2026-08-10', 'Peliyagoda Yard', 'Pending verification of stolen accessories and minor body scratches.', 'MODERATE', 110000.00, 0);

INSERT INTO pre_policy_inspections (id, odometer_verified, body_condition_rating, mechanical_score) VALUES
(1, 1, 9, 10);

INSERT INTO damage_assessments (id, structural_damage_found, parts_to_replace, labor_cost_estimate, recommended_action) VALUES
(2, 0, 'Front Bumper Cover, Fog Lamp Bracket Assembly', 20000.00, 'REPAIR_AUTHORIZED'),
(3, 0, 'Spare Wheel Carrier, Left Side Mirror Assembly', 15000.00, 'PENDING_POLICE_CLEARANCE');
