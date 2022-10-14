package carsharing.dao;

import carsharing.H2Utils;
import carsharing.domain.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompanyJpaImpl implements Jpa<Company> {

    private final List<Company> companies = new ArrayList<>();
    static private final Connection connection = H2Utils.getConnection();

    private static final String SELECT_FROM_COMPANY_TABLE = "SELECT * FROM COMPANY";
    private static final String WHERE_NAME = " WHERE NAME =?";
    private static final String WHERE_ID = " WHERE ID =?";
    private static final String INSERT_NAME_INTO_COMPANY_TABLE = "INSERT INTO COMPANY" +
            " (NAME) VALUES" +
            " (?);";

    public CompanyJpaImpl() {
    }

    @Override
    public Optional<Company> findById(long id) {
        Optional<Company> company = Optional.empty();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_FROM_COMPANY_TABLE + WHERE_ID)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int receivedId = rs.getInt("id");
                String receivedName = rs.getString("name");
                company = Optional.of(new Company(receivedId, receivedName));
            }

        } catch (Exception e) {
            System.out.println("Exception occured while finding by id");
        }
        return company;
    }

    @Override
    public Optional<Company> findByName(String name) {
        Optional<Company> company = Optional.empty();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_FROM_COMPANY_TABLE + WHERE_NAME)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String receivedName = rs.getString("name");
                company = Optional.of(new Company(id, receivedName));
            }

        } catch (Exception e) {
            System.out.println("Exception occured while finding by name");
        }
        return company;
    }

    @Override
    public List<Company> findAll() {
        try {
            ResultSet rs = connection.createStatement().executeQuery(SELECT_FROM_COMPANY_TABLE);
            companies.clear();
            while (rs.next()) {
                int id = rs.getInt("id");
                String receivedName = rs.getString("name");
                companies.add(new Company(id, receivedName));
            }
        } catch (Exception e) {
            System.out.println("Exception occured while finding all");
        }
        return companies;
    }

    @Override
    public Company insert(Company entity) {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_NAME_INTO_COMPANY_TABLE)) {
            ps.setString(1, entity.getName());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Exception occured while inserting");
        }
        return findByName(entity.getName()).orElse(null);
    }

    @Override
    public boolean delete(long id) {
        //not yet to be implemented
        return false;
    }

    @Override
    public boolean update(Company entity) {
        //not yet to be implemented
        return false;
    }
}
