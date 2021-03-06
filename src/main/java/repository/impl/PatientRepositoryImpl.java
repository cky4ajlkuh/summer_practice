package repository.impl;

import config.ApplicationDataSource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;
import players.Patient;
import repository.PatientRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Repository
public class PatientRepositoryImpl implements PatientRepository {

    @Override
    public void save(Patient patient) {
        try (PreparedStatement statement = ApplicationDataSource.getConnection()
                .prepareStatement("insert into patient (name, emailOwner) values ('" + patient.getName() + "', '" +
                        patient.getEmailOwner() + "')")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Patient patient) {
        try (PreparedStatement statement = ApplicationDataSource.getConnection()
                .prepareStatement("delete from patient where id = " + patient.getId())) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<Patient> getPatients() {
        try (PreparedStatement statement = ApplicationDataSource.getConnection().prepareStatement("select * from patient")) {
            ResultSet set = statement.executeQuery();
            return mapResultSetToPatient(set);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private static Set<Patient> mapResultSetToPatient(ResultSet set) {
        Set<Patient> patients = new HashSet<>();
        while (set.next()) {
            int id = set.getInt("id");
            String name = set.getString("name");
            String emailOwner = set.getString("emailOwner");
            Patient patient = new Patient(name, emailOwner);
            patient.setId(id);
            patients.add(patient);
        }
        return patients;
    }

    @Override
    public void changeName(Patient patient, String name) {
        try (PreparedStatement statement = ApplicationDataSource.getConnection()
                .prepareStatement("UPDATE patient SET name = '" + name + "' where id = " + patient.getId())) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
