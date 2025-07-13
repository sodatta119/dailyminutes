package com.dailyminutes.laundry.customer.repository;

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.customer.domain.model.CustomerAddressEntity;
import com.dailyminutes.laundry.customer.domain.model.AddressType;
import com.dailyminutes.laundry.customer.domain.model.CustomerEntity;
import com.dailyminutes.laundry.geofence.domain.model.GeofenceEntity;
import com.dailyminutes.laundry.geofence.repository.GeofenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Customer address repository test.
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = {"com.dailyminutes.laundry.customer.repository",
        "com.dailyminutes.laundry.geofence.repository"})
@ComponentScan(basePackages = {"com.dailyminutes.laundry.customer.domain.model",
        "com.dailyminutes.laundry.customer.domain.model"})
class CustomerAddressRepositoryTest {

    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GeofenceRepository geofenceRepository;

    private GeofenceEntity geofence;
    private CustomerEntity customer;

    /**
     * Setup.
     */
    @BeforeEach
    void setup(){
        geofence = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));
        customer = customerRepository.save(new CustomerEntity(null, "SUB123", "9876543210", "Jane Doe", "jane@example.com"));
    }

    /**
     * Test save and find customer address.
     */
    @Test
    void testSaveAndFindCustomerAddress() {
        CustomerAddressEntity address = new CustomerAddressEntity(
                null, customer.getId(), AddressType.HOME, true, "Apt 101", "123 Main St", "Main Street", "Springfield", "IL", "62704", "USA", "39.79", "-89.65", geofence.getId());
        CustomerAddressEntity savedAddress = customerAddressRepository.save(address);

        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getId()).isNotNull();
        assertThat(savedAddress.getCustomerId()).isEqualTo(customer.getId());
        assertThat(savedAddress.getAddressType()).isEqualTo(AddressType.HOME);
        assertThat(savedAddress.isDefault()).isTrue();
        assertThat(savedAddress.getFlatApartment()).isEqualTo("Apt 101");
        assertThat(savedAddress.getAddressLine()).isEqualTo("123 Main St");
        assertThat(savedAddress.getStreet()).isEqualTo("Main Street");
        assertThat(savedAddress.getCity()).isEqualTo("Springfield");
        assertThat(savedAddress.getState()).isEqualTo("IL");
        assertThat(savedAddress.getZipCode()).isEqualTo("62704");
        assertThat(savedAddress.getCountry()).isEqualTo("USA");
        assertThat(savedAddress.getGeofenceId()).isEqualTo(geofence.getId());

        Optional<CustomerAddressEntity> foundAddress = customerAddressRepository.findById(savedAddress.getId());
        assertThat(foundAddress).isPresent();
        assertThat(foundAddress.get().getCity()).isEqualTo("Springfield");
    }

    /**
     * Test save address with nullable fields null.
     */
    @Test
    void testSaveAddressWithNullableFieldsNull() {
        CustomerAddressEntity address = new CustomerAddressEntity(
                null, customer.getId(), AddressType.WORK, false, null, "456 Oak Ave", null, null, null, null, null, "40.71", "-74.00", geofence.getId());
        CustomerAddressEntity savedAddress = customerAddressRepository.save(address);

        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getId()).isNotNull();
        assertThat(savedAddress.getFlatApartment()).isNull();
        assertThat(savedAddress.getAddressLine()).isEqualTo("456 Oak Ave");
        assertThat(savedAddress.getStreet()).isNull();
        assertThat(savedAddress.getCity()).isNull();
        assertThat(savedAddress.getState()).isNull();
        assertThat(savedAddress.getZipCode()).isNull();
        assertThat(savedAddress.getCountry()).isNull();
    }

    /**
     * Test update customer address.
     */
    @Test
    void testUpdateCustomerAddress() {
        CustomerAddressEntity address = new CustomerAddressEntity(
                null, customer.getId(), AddressType.WORK, false, "Suite 200", "456 Oak Ave", "Oak Avenue", "Metropolis", "NY", "10001", "USA", "40.71", "-74.00", geofence.getId());
        CustomerAddressEntity savedAddress = customerAddressRepository.save(address);

        savedAddress.setFlatApartment("Unit 300");
        savedAddress.setAddressLine("789 Pine Ln");
        savedAddress.setCity("Gotham");
        savedAddress.setDefault(true); // Change to default
        customerAddressRepository.save(savedAddress);

        Optional<CustomerAddressEntity> updatedAddress = customerAddressRepository.findById(savedAddress.getId());
        assertThat(updatedAddress).isPresent();
        assertThat(updatedAddress.get().getFlatApartment()).isEqualTo("Unit 300");
        assertThat(updatedAddress.get().getAddressLine()).isEqualTo("789 Pine Ln");
        assertThat(updatedAddress.get().getCity()).isEqualTo("Gotham");
        assertThat(updatedAddress.get().isDefault()).isTrue();
    }

    /**
     * Test delete customer address.
     */
    @Test
    void testDeleteCustomerAddress() {
        CustomerAddressEntity address = new CustomerAddressEntity(
                null, customer.getId(), AddressType.OTHER, false, null, "999 Test Rd", null, "Testville", "CA", "90210", "USA", "34.05", "-118.25", geofence.getId());
        CustomerAddressEntity savedAddress = customerAddressRepository.save(address);

        customerAddressRepository.deleteById(savedAddress.getId());
        Optional<CustomerAddressEntity> deletedAddress = customerAddressRepository.findById(savedAddress.getId());
        assertThat(deletedAddress).isNotPresent();
    }

    /**
     * Test find by customer id.
     */
    @Test
    void testFindByCustomerId() {
        GeofenceEntity geofence1 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));
        CustomerEntity customer1 = customerRepository.save(new CustomerEntity(null, "SUB124", "3433423423", "Jane Doe", "jane@example.com"));
        CustomerEntity customer2 = customerRepository.save(new CustomerEntity(null, "SUB125", "3434343434", "Jane Doe", "jane@example.com"));

        customerAddressRepository.save(new CustomerAddressEntity(null, customer1.getId(), AddressType.HOME, true, "Apt A", "Addr A", "St A", "City A", "ST", "11111", "USA", "1.0", "1.0", geofence1.getId()));
        customerAddressRepository.save(new CustomerAddressEntity(null, customer1.getId(), AddressType.WORK, false, null, "Addr B", "St B", "City B", "ST", "22222", "USA", "2.0", "2.0", geofence1.getId()));
        customerAddressRepository.save(new CustomerAddressEntity(null, customer2.getId(), AddressType.HOME, true, "Apt C", "Addr C", "St C", "City C", "ST", "33333", "USA", "3.0", "3.0", geofence1.getId()));

        List<CustomerAddressEntity> addressesForCustomer5 = customerAddressRepository.findByCustomerId(customer1.getId());
        assertThat(addressesForCustomer5).hasSize(2);
        assertThat(addressesForCustomer5.stream().allMatch(a -> a.getCustomerId().equals(customer1.getId()))).isTrue();
    }

    /**
     * Test find by customer id and is default true.
     */
    @Test
    void testFindByCustomerIdAndIsDefaultTrue() {
        GeofenceEntity geofence1 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));
        GeofenceEntity geofence2 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));
        CustomerEntity customer1 = customerRepository.save(new CustomerEntity(null, "SUB124", "3433423423", "Jane Doe", "jane@example.com"));
        CustomerEntity customer2 = customerRepository.save(new CustomerEntity(null, "SUB125", "3434343434", "Jane Doe", "jane@example.com"));

        customerAddressRepository.save(new CustomerAddressEntity(null, customer1.getId(), AddressType.HOME, true, "Apt D", "Default Home", "DefSt", "DefCity", "DF", "00000", "USA", "10.0", "10.0", geofence1.getId()));
        customerAddressRepository.save(new CustomerAddressEntity(null, customer1.getId(), AddressType.WORK, false, null, "Work Addr", "WorkSt", "WorkCity", "WK", "11111", "USA", "11.0", "11.0", geofence1.getId()));
        customerAddressRepository.save(new CustomerAddressEntity(null, customer2.getId(), AddressType.HOME, true, "Apt E", "Another Default", "AnSt", "AnCity", "AN", "22222", "USA", "12.0", "12.0", geofence2.getId()));

        Optional<CustomerAddressEntity> defaultAddressForCustomer7 = customerAddressRepository.findByCustomerIdAndIsDefaultTrue(customer1.getId());
        assertThat(defaultAddressForCustomer7).isPresent();
        assertThat(defaultAddressForCustomer7.get().getAddressLine()).isEqualTo("Default Home");
        assertThat(defaultAddressForCustomer7.get().isDefault()).isTrue();
    }

    /**
     * Test find by geofence id.
     */
    @Test
    void testFindByGeofenceId() {
        GeofenceEntity geofence1 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));
        GeofenceEntity geofence2 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));
        CustomerEntity customer1 = customerRepository.save(new CustomerEntity(null, "SUB124", "3433423423", "Jane Doe", "jane@example.com"));
        CustomerEntity customer2 = customerRepository.save(new CustomerEntity(null, "SUB125", "3434343434", "Jane Doe", "jane@example.com"));

        customerAddressRepository.save(new CustomerAddressEntity(null, customer1.getId(), AddressType.HOME, true, "Apt F", "GF Addr 1", "GF St", "GF City", "GF", "40001", "USA", "20.0", "20.0", geofence1.getId()));
        customerAddressRepository.save(new CustomerAddressEntity(null, customer2.getId(), AddressType.WORK, false, null, "GF Addr 2", "GF St", "GF City", "GF", "40002", "USA", "21.0", "21.0", geofence1.getId()));
        customerAddressRepository.save(new CustomerAddressEntity(null, customer2.getId(), AddressType.OTHER, false, "Apt G", "GF Addr 3", "GF St", "GF City", "GF", "40003", "USA", "22.0", "22.0", geofence2.getId()));

        List<CustomerAddressEntity> addressesInGeofence401 = customerAddressRepository.findByGeofenceId(geofence1.getId());
        assertThat(addressesInGeofence401).hasSize(2);
        assertThat(addressesInGeofence401.stream().allMatch(a -> a.getGeofenceId().equals(customer1.getId()))).isTrue();
    }

    /**
     * Test find by customer id and address type.
     */
    @Test
    void testFindByCustomerIdAndAddressType() {
        GeofenceEntity geofence1 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));
        GeofenceEntity geofence2 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));
        CustomerEntity customer1 = customerRepository.save(new CustomerEntity(null, "SUB124", "3433423423", "Jane Doe", "jane@example.com"));
        CustomerEntity customer2 = customerRepository.save(new CustomerEntity(null, "SUB125", "3434343434", "Jane Doe", "jane@example.com"));

        customerAddressRepository.save(new CustomerAddressEntity(null, customer1.getId(), AddressType.HOME, true, "Apt H", "Home A", "Home St", "City X", "CX", "50001", "USA", "30.0", "30.0", geofence1.getId()));
        customerAddressRepository.save(new CustomerAddressEntity(null, customer1.getId(), AddressType.OTHER, false, null, "Billing A", "Billing St", "City Y", "CY", "50002", "USA", "31.0", "31.0", geofence2.getId()));
        customerAddressRepository.save(new CustomerAddressEntity(null, customer2.getId(), AddressType.HOME, true, "Apt I", "Home B", "Home St B", "City Z", "CZ", "50003", "USA", "32.0", "32.0", geofence1.getId()));

        List<CustomerAddressEntity> customer12HomeAddresses = customerAddressRepository.findByCustomerIdAndAddressType(customer1.getId(), AddressType.HOME);
        assertThat(customer12HomeAddresses).hasSize(1);
        assertThat(customer12HomeAddresses.get(0).getAddressLine()).isEqualTo("Home A");
        assertThat(customer12HomeAddresses.get(0).getAddressType()).isEqualTo(AddressType.HOME);
    }
}
