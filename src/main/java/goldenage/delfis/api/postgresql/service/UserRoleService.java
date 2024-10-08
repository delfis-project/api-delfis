/*
 * Classe UserRoleService
 * Service da entidade UserRole
 * Autor: João Diniz Araujo
 * Data: 13/08/2024
 * */

package goldenage.delfis.api.postgresql.service;

import goldenage.delfis.api.postgresql.model.UserRole;
import goldenage.delfis.api.postgresql.repository.UserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    /**
     * @return todos os userRoles do banco.
     */
    public List<UserRole> getUserRoles() {
        List<UserRole> userRoles = userRoleRepository.findAll();
        return userRoles.isEmpty() ? null : userRoles;
    }

    /**
     * @return userRole pelo id.
     */
    public UserRole getUserRoleById(Long id) {
        Optional<UserRole> userRole = userRoleRepository.findById(id);
        return userRole.orElse(null);  // vai retornar userRole se ele existir, se não retorna null
    }

    /**
     * @return userRole deletado.
     */
    public UserRole deleteUserRoleById(Long id) {
        UserRole userRole = getUserRoleById(id);
        if (userRole != null) userRoleRepository.deleteById(id);
        return userRole;
    }

    /**
     * @return userRole inserido.
     */
    public UserRole saveUserRole(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }
}
