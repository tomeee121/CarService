package carsharing.dao;

import carsharing.H2Utils;
import carsharing.domain.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerJpaImpl implements Jpa<Customer>{

    private final List<Customer> customerList = new ArrayList<>();
    static private final Connection connection = H2Utils.getConnection();

    private static final String SELECT_FROM_CUSTOMER_TABLE = "SELECT * FROM CUSTOMER";
    private static final String WHERE_NAME = " WHERE NAME =?";
    private static final String WHERE_ID = " WHERE ID =?";
    private static final String INSERT_NAME_INTO_CUSTOMER_TABLE = "INSERT INTO CUSTOMER(NAME, RENTED_CAR_ID)" +
            " VALUES (?,?);";

    private static final String UPDATE_CUSTOMER_SET_RENTED_CAR = "UPDATE CUSTOMER " +
            "SET RENTED_CAR_ID = ? ";


    @Override
    public Optional<Customer> findById(long id) {
        Optional<Customer> customers = Optional.empty();
        try (final PreparedStatement ps = connection.prepareStatement(SELECT_FROM_CUSTOMER_TABLE + WHERE_ID)) {
            ps.setLong(1, id);

            final ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                customers = Optional.of(new Customer(resultSet.getInt("ID"), resultSet.getString("NAME"), resultSet.getInt("RENTED_CAR_ID")));
            }

        } catch (SQLException e) {
            System.out.println("Exception occurred while finding Car by ID");
        }
        return customers;
    }

    @Override
    public Optional<Customer> findByName(String name) {
        Optional<Customer> customers = Optional.empty();
        try (final PreparedStatement ps = connection.prepareStatement(SELECT_FROM_CUSTOMER_TABLE + WHERE_NAME)) {
            ps.setString(1, name);

            final ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                customers = Optional.of(new Customer(resultSet.getInt("ID"), resultSet.getString("NAME"), resultSet.getInt("RENTED_CAR_ID")));
            }

        } catch (SQLException e) {
            System.out.println("Exception occurred while finding Car by Name");
        }
        return customers;
    }

    @Override
    public List<Customer> findAll() {
        customerList.clear();
        try (final PreparedStatement ps = connection.prepareStatement(SELECT_FROM_CUSTOMER_TABLE)) {
            final ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                customerList.add(new Customer(resultSet.getInt("ID"), resultSet.getString("NAME"), resultSet.getInt("RENTED_CAR_ID")));
            }

        } catch (SQLException e) {
            System.out.println("Exception occurred while finding all in Car");
        }
        return customerList;
    }

    @Override
    public Customer insert(Customer entity) {
        try (final PreparedStatement ps = connection.prepareStatement(INSERT_NAME_INTO_CUSTOMER_TABLE)) {
            ps.setString(1, entity.getName());
            ps.setNull(2, Types.INTEGER);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Exception occurred while inserting into Customer");
        }
        return findByName(entity.getName()).orElse(null);
    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    @Override
    public boolean update(Customer entity) {
        return false;
    }

    public boolean deleteRentedCarFromCustomer(Customer customer) {
        try (final PreparedStatement ps = connection.prepareStatement(UPDATE_CUSTOMER_SET_RENTED_CAR + WHERE_ID)){
            ps.setNull(1, Types.INTEGER);
            ps.setInt(2,customer.getId());
            ps.executeUpdate();
            return true;
        } catch(Exception e){
            System.out.println("Exception occurred while updating customer rented car to null");
        }
        return false;
    }

    public boolean insertRentedCar(Customer customer, int id) {
        try (final PreparedStatement ps = connection.prepareStatement(UPDATE_CUSTOMER_SET_RENTED_CAR + WHERE_ID)){
            ps.setInt(1, id);
            ps.setInt(2,customer.getId());
            ps.executeUpdate();
            return true;
        } catch(Exception e){
            System.out.println("Exception occurred while updating customer rented car to null");
        }
        return false;
    }
}
