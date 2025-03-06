package dmit2015.service;

import dmit2015.model.Student;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.security.enterprise.SecurityContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Named("jakartaPersistenceStudentService")
@ApplicationScoped
public class JakartaPersistenceStudentService implements StudentService {

    @Inject
    private SecurityContext _securityContext;

    // Assign a unitName if there are more than one persistence unit defined in persistence.xml
    @PersistenceContext (unitName="postgresql-jpa-pu")
    private EntityManager _entityManager;

    @Override
    @Transactional
    public Student createStudent(Student student) {
       // Grant access only to auth users with the Sales or DMIT2015.1242.OE01 role.
        boolean hasRequiredRole = _securityContext.isCallerInRole("Sales")
                || _securityContext.isCallerInRole("DMIT2015.1242.OE01");
        if (!hasRequiredRole) {
            throw new SecurityException("Access denied. You do not have enough permissions to perform this action.");
        }
        // Assign the username who create the student
        String username = _securityContext.getCallerPrincipal().getName();
        student.setUsername(username);

        _entityManager.persist(student);
        return student;
    }

    @Override
    public Optional<Student> getStudentById(Long id) {
        try {
            Student querySingleResult = _entityManager.find(Student.class, id);
            if (querySingleResult != null) {
                return Optional.of(querySingleResult);
            }
        } catch (Exception ex) {
            // id value not found
            throw new RuntimeException(ex);
        }
        return Optional.empty();
    }

    @Override
    public List<Student> getAllStudents() {
        // Grant access only to auth users with the Sales or DMIT2015.1242.OE01 role.
        boolean hasRequiredRole = _securityContext.isCallerInRole("Sales")
                || _securityContext.isCallerInRole("DMIT2015.1242.OE01");
        if (!hasRequiredRole) {
            throw new SecurityException("Access denied. You do not have enough permissions to perform this action.");
        }

        // For the DMIT2015.1242.OE1 role return only Student data
        // created create the current auth user.
        String username = _securityContext.getCallerPrincipal().getName();
        boolean hasDmit2015Role = _securityContext.isCallerInRole("DMIT2015.1242.OE01");
        if (hasDmit2015Role) {
            // Return Student data created by the current username
            return _entityManager.createQuery(
                    "select o from Student o where o.username = :usernameValue",
                    Student.class)
                    .setParameter("usernameValue", username)
                    .getResultList();
        }
        return _entityManager.createQuery("SELECT o FROM Student o ", Student.class)
                .getResultList();
    }

    @Override
    @Transactional
    public Student updateStudent(Student student) {

        Optional<Student> optionalStudent = getStudentById(student.getId());
        if (optionalStudent.isEmpty()) {
            String errorMessage = String.format("The id %s does not exists in the system.", student.getId());
            throw new RuntimeException(errorMessage);
        } else {
            var existingStudent = optionalStudent.orElseThrow();
            // Update only properties that is editable by the end user

              existingStudent.setFirstName(student.getFirstName());
              existingStudent.setLastName(student.getLastName());
              existingStudent.setCourseSection(student.getCourseSection());
              existingStudent.setPicture(student.getPicture());

            student = _entityManager.merge(existingStudent);
        }
        return student;
    }

    @Override
    @Transactional
    public void deleteStudentById(Long id) {
        Optional<Student> optionalStudent = getStudentById(id);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.orElseThrow();
            // Write code to throw a RuntimeException if this entity contains child records
            _entityManager.remove(student);
        } else {
            throw new RuntimeException("Could not find Student with id: " + id);
        }
    }

    @Override
    @Transactional
    public void deleteAllStudents() {
        _entityManager.createQuery("DELETE FROM Student").executeUpdate();
    }

}