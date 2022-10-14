package carsharing.dao;

import carsharing.H2Utils;
import carsharing.domain.Car;
import carsharing.domain.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarJpaImpl implements Jpa<Car> {

    private final List<Car> carsList = new ArrayList<>();
    static private final Connection connection = H2Utils.getConnection();

    private static final String SELECT_FROM_CAR_TABLE = "SELECT * FROM CAR";
    private static final String WHERE_NAME = " WHERE NAME =?";
    private static final String WHERE_ID = " WHERE ID =?";
    private static final String INSERT_NAME_INTO_CAR_TABLE = "INSERT INTO CAR(NAME,COMPANY_ID)" +
            " VALUES (?,?);";

    private static final String WHERE_COMPANY_ID = " WHERE COMPANY_ID =?";


    public CarJpaImpl() {
    }

    public List<Car> findByCompany(Company company) {
        carsList.clear();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_FROM_CAR_TABLE + WHERE_COMPANY_ID)) {
            ps.setInt(1, company.getId());
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                carsList.add(new Car(resultSet.getInt("ID"), resultSet.getString("NAME"), resultSet.getInt("COMPANY_ID")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return carsList;
    }

    @Override
    public Optional<Car> findById(long id) {
        Optional<Car> cars = Optional.empty();
        try (final PreparedStatement ps = connection.prepareStatement(SELECT_FROM_CAR_TABLE + WHERE_ID)) {
            ps.setLong(1, id);

            final ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                cars = Optional.of(new Car(resultSet.getInt("ID"), resultSet.getString("NAME"), resultSet.getInt("COMPANY_ID")));
            }

        } catch (SQLException e) {
            System.out.println("Exception occurred while finding Car by ID");
        }
        return cars;
    }

    @Override
    public Optional<Car> findByName(String name) {
        Optional<Car> cars = Optional.empty();
        try (final PreparedStatement ps = connection.prepareStatement(SELECT_FROM_CAR_TABLE + WHERE_NAME)) {
            ps.setString(1, name);

            final ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                cars = Optional.of(new Car(resultSet.getInt("ID"), resultSet.getString("NAME"), resultSet.getInt("COMPANY_ID")));
            }

        } catch (SQLException e) {
            System.out.println("Exception occurred while finding Car by Name");
        }
        return cars;
    }

    @Override
    public List<Car> findAll() {
        carsList.clear();
        try (final PreparedStatement ps = connection.prepareStatement(SELECT_FROM_CAR_TABLE)) {
            final ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                carsList.add(new Car(resultSet.getInt("ID"), resultSet.getString("NAME"), resultSet.getInt("COMPANY_ID")));
            }

        } catch (SQLException e) {
            System.out.println("Exception occurred while finding all in Car");
        }
        return carsList;
    }


    @Override
    public Car insert(Car entity) {
        try (final PreparedStatement ps = connection.prepareStatement(INSERT_NAME_INTO_CAR_TABLE)) {
            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getCompanyId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Exception occurred while inserting into Car");
        }
        return findByName(entity.getName()).orElse(null);
    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    @Override
    public boolean update(Car entity) {
        return false;
    }
}
