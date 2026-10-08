package com.saferide.insurance.config;

import com.saferide.insurance.entity.claim.*;
import com.saferide.insurance.entity.customer.CustomerProfile;
import com.saferide.insurance.entity.inspection.*;
import com.saferide.insurance.entity.policy.*;
import com.saferide.insurance.entity.quote.*;
import com.saferide.insurance.entity.user.*;
import com.saferide.insurance.entity.vehicle.*;
import com.saferide.insurance.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * SafeRide Automated Data Initializer
 * Populates database with sample operational data across all 6 student modules on initial launch.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CustomerProfileRepository profileRepository;
    private final VehicleRepository vehicleRepository;
    private final PolicyRepository policyRepository;
    private final PremiumQuoteRepository quoteRepository;
    private final ClaimRepository claimRepository;
    private final InspectionRepository inspectionRepository;

    public DataInitializer(UserRepository userRepository,
                           CustomerProfileRepository profileRepository,
                           VehicleRepository vehicleRepository,
                           PolicyRepository policyRepository,
                           PremiumQuoteRepository quoteRepository,
                           ClaimRepository claimRepository,
                           InspectionRepository inspectionRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.vehicleRepository = vehicleRepository;
        this.policyRepository = policyRepository;
        this.quoteRepository = quoteRepository;
        this.claimRepository = claimRepository;
        this.inspectionRepository = inspectionRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return; // Data already seeded
        }

        System.out.println("🌱 Initializing SafeRide Insurance System Demo Dataset...");

        // 1. Users
        Admin admin = new Admin("admin", "admin123", "admin@saferide.com", "Ranil Wickramasinghe", "+94771110001", "SUPER_ADMIN", "Executive Operations");
        InsuranceAgent agent = new InsuranceAgent("agent_kamal", "agent123", "kamal.agent@saferide.com", "Kamal Perera", "+94772220002", "AG-LK-2026-9901", "Colombo Central", new BigDecimal("6.50"));
        ClaimsOfficer officer = new ClaimsOfficer("officer_nimal", "officer123", "nimal.claims@saferide.com", "Nimal Silva", "+94773330003", "EMP-CLM-4402", "Motor Claims", new BigDecimal("1000000.00"));
        FinanceOfficer finance = new FinanceOfficer("finance_priya", "finance123", "priya.finance@saferide.com", "Priya Fernando", "+94776660006", "FIN-ACC-3301", "Finance & Payments", new BigDecimal("2000000.00"));
        BranchManager manager = new BranchManager("manager_rohan", "manager123", "rohan.manager@saferide.com", "Rohan Wickrama", "+94777770007", "MGR-CMB-1001", "Colombo Main Branch", "BR-CMB-001");
        Customer customer1 = new Customer("customer_sunil", "customer123", "sunil.fernando@gmail.com", "Sunil Fernando", "+94774440004");
        Customer customer2 = new Customer("customer_anusha", "customer123", "anusha.de.silva@gmail.com", "Anusha De Silva", "+94775550005");

        userRepository.save(admin);
        userRepository.save(agent);
        userRepository.save(officer);
        userRepository.save(finance);
        userRepository.save(manager);
        Customer savedCust1 = userRepository.save(customer1);
        Customer savedCust2 = userRepository.save(customer2);

        // 2. Customer Profiles (Deshanya T.)
        CustomerProfile prof1 = new CustomerProfile(savedCust1, "198812304567", LocalDate.of(1988, 5, 14), "B8849201", LocalDate.of(2028, 5, 14), "45/2 Temple Road", "Colombo 07", "00700", "Senior Software Architect", "Chathuri Fernando", "+94778889901");
        CustomerProfile prof2 = new CustomerProfile(savedCust2, "199267891234", LocalDate.of(1992, 9, 22), "B9210982", LocalDate.of(2030, 9, 22), "128 Galle Road", "Mount Lavinia", "10370", "Marketing Consultant", "Rohan De Silva", "+94778889902");
        profileRepository.save(prof1);
        profileRepository.save(prof2);

        // 3. Vehicles (Ravihara L.D.C.)
        PrivateCar car1 = new PrivateCar(savedCust1, "Toyota", "Prius Hybrid", 2021, "WP-CAB-4521", "JTDKN36U801928374", 1800, "HYBRID", new BigDecimal("12500000.00"), 32000, 5, true, "SEDAN");
        Motorcycle bike1 = new Motorcycle(savedCust1, "Yamaha", "FZ-S V3", 2022, "WP-BFF-8841", "ME4RG123891029384", 150, "PETROL", new BigDecimal("950000.00"), 14500, true, "FOUR_STROKE");
        CommercialVehicle truck1 = new CommercialVehicle(savedCust2, "Isuzu", "Elf Light Truck", 2019, "WP-ND-6019", "ISZ98172648102938", 2990, "DIESEL", new BigDecimal("8200000.00"), 85000, new BigDecimal("3.50"), "GP-WP-88192", "LOGISTICS");

        PrivateCar savedCar = vehicleRepository.save(car1);
        Motorcycle savedBike = vehicleRepository.save(bike1);
        CommercialVehicle savedTruck = vehicleRepository.save(truck1);

        // 4. Policies (Gamage S.M.)
        ComprehensivePolicy pol1 = new ComprehensivePolicy("POL-2026-COMP-001", savedCust1, savedCar, PolicyStatus.ACTIVE, LocalDate.of(2026, 1, 1), LocalDate.of(2027, 1, 1), new BigDecimal("12500000.00"), new BigDecimal("150000.00"), new BigDecimal("162500.00"), new BigDecimal("10000.00"), true, true, true, "PLATINUM");
        ThirdPartyPolicy pol2 = new ThirdPartyPolicy("POL-2026-TP-002", savedCust1, savedBike, PolicyStatus.ACTIVE, LocalDate.of(2026, 2, 15), LocalDate.of(2027, 2, 15), new BigDecimal("950000.00"), new BigDecimal("18000.00"), new BigDecimal("21500.00"), new BigDecimal("2500.00"), new BigDecimal("500000.00"), true);
        ComprehensivePolicy pol3 = new ComprehensivePolicy("POL-2026-COMP-003", savedCust2, savedTruck, PolicyStatus.ACTIVE, LocalDate.of(2026, 3, 10), LocalDate.of(2027, 3, 10), new BigDecimal("8200000.00"), new BigDecimal("120000.00"), new BigDecimal("134000.00"), new BigDecimal("15000.00"), true, true, false, "GOLD");

        Policy savedPol1 = policyRepository.save(pol1);
        policyRepository.save(pol2);
        Policy savedPol3 = policyRepository.save(pol3);

        // 5. Premium Quotes (Subasinghe S.A.K.G.)
        StandardQuote quote1 = new StandardQuote("QUO-2026-88192", savedCust1, savedCar, PolicyType.COMPREHENSIVE, new BigDecimal("12500000.00"), new BigDecimal("0.0120"), new BigDecimal("5.00"), new BigDecimal("20.00"), LocalDate.of(2026, 12, 31), new BigDecimal("1500.00"));
        DiscountedQuote quote2 = new DiscountedQuote("QUO-2026-99041", savedCust2, savedTruck, PolicyType.COMPREHENSIVE, new BigDecimal("8200000.00"), new BigDecimal("0.0145"), new BigDecimal("10.00"), new BigDecimal("15.00"), LocalDate.of(2026, 12, 31), "SAFEPROMO2026", new BigDecimal("7.50"), 3);
        quote1.setAppliedToPolicy(true);
        quote2.setAppliedToPolicy(true);
        quoteRepository.save(quote1);
        quoteRepository.save(quote2);

        // 6. Claims (Arachchi A.M.R.S. - Lead)
        AccidentClaim claim1 = new AccidentClaim("CLM-2026-ACC-001", savedPol1, savedCust1, ClaimStatus.UNDER_REVIEW, LocalDate.of(2026, 6, 12), "High Level Road, Nugegoda", "Front bumper collided with roadside curb during heavy rain.", new BigDecimal("85000.00"), false, "POL-NUG-2026-881", "Sunil Fernando", "SINGLE_VEHICLE_OBSTACLE");
        claim1.setAssessorNotes("Assessor verified front bumper scratch and fog light bracket fracture. Payout adjusted.");
        claim1.setApprovedPayoutAmount(new BigDecimal("75000.00"));

        WindscreenClaim claim2 = new WindscreenClaim("CLM-2026-WSC-002", savedPol1, savedCust1, ClaimStatus.APPROVED, LocalDate.of(2026, 7, 4), "Southern Expressway Km 32", "Flying pebble caused severe crack on the front windshield.", new BigDecimal("45000.00"), "FRONT_WINDSCREEN", "Toyota Lanka Wattala", true);
        claim2.setApprovedPayoutAmount(new BigDecimal("45000.00"));
        claim2.setSettlementDate(LocalDate.of(2026, 7, 6));
        claim2.setAssessorNotes("Windscreen replacement authorized at authorized Toyota Center.");

        TheftClaim claim3 = new TheftClaim("CLM-2026-THF-003", savedPol3, savedCust2, ClaimStatus.INSPECTION_SCHEDULED, LocalDate.of(2026, 8, 1), "Peliyagoda Market Yard", "Spare wheel and side mirrors stolen while vehicle was parked overnight.", new BigDecimal("110000.00"), "Peliyagoda Police Station", "CR-PEL-2026-442", "NOT_RECOVERED", "Peliyagoda Commercial Zone");
        claim3.setAssessorNotes("Police extract obtained. Physical damage inspection booked.");

        Claim savedClaim1 = claimRepository.save(claim1);
        claimRepository.save(claim2);
        Claim savedClaim3 = claimRepository.save(claim3);

        // 7. Inspections (Senavirathna M.D.T.D.)
        PrePolicyInspection insp1 = new PrePolicyInspection("INSP-2026-PRE-001", savedCar, officer, InspectionStatus.COMPLETED, LocalDate.of(2025, 12, 28), "Colombo Central Yard", "Vehicle in pristine condition, genuine low mileage verified.", true, true, 9, 10);
        DamageAssessment insp2 = new DamageAssessment("INSP-2026-DMG-002", savedCar, savedClaim1, officer, InspectionStatus.COMPLETED, LocalDate.of(2026, 6, 14), "Nugegoda Service Center", "Front bumper cover cracked, lower radiator grille intact, sensors functioning.", "MINOR", new BigDecimal("85000.00"), true, false, "Front Bumper Cover, Fog Lamp Bracket Assembly", new BigDecimal("20000.00"), "REPAIR_AUTHORIZED");
        DamageAssessment insp3 = new DamageAssessment("INSP-2026-DMG-003", savedTruck, savedClaim3, officer, InspectionStatus.PENDING, LocalDate.of(2026, 8, 10), "Peliyagoda Yard", "Pending verification of stolen accessories and minor body scratches.", "MODERATE", new BigDecimal("110000.00"), false, false, "Spare Wheel Carrier, Left Side Mirror Assembly", new BigDecimal("15000.00"), "PENDING_POLICE_CLEARANCE");

        inspectionRepository.save(insp1);
        inspectionRepository.save(insp2);
        inspectionRepository.save(insp3);

        System.out.println("✅ SafeRide Insurance System Seed Data Loaded Successfully!");
    }
}
