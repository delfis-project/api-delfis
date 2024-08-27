/*
 * Classe AppUserController
 * Controller da entidade AppUser
 * Autor: João Diniz Araujo
 * Data: 16/08/2024
 */

package goldenage.delfis.apiusersql.controller;

import goldenage.delfis.apiusersql.model.AppUser;
import goldenage.delfis.apiusersql.model.Plan;
import goldenage.delfis.apiusersql.model.UserRole;
import goldenage.delfis.apiusersql.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/app-user")
public class AppUserController {
    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/get-all")
    @Operation(summary = "Obter todos os usuários", description = "Retorna uma lista de todos os usuários registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários encontrada", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AppUser.class)))),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado", content = @Content)
    })
    public ResponseEntity<?> getAppUsers() {
        List<AppUser> users = appUserService.getAppUsers();
        if (!users.isEmpty()) return ResponseEntity.status(HttpStatus.OK).body(users);

        throw new EntityNotFoundException("Nenhum usuário encontrado.");
    }

    @GetMapping("/get-by-username/{username}")
    @Operation(summary = "Obter usuário por nome de usuário", description = "Retorna um usuário baseado no seu nome de usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado", content = @Content(schema = @Schema(implementation = AppUser.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Argumento inválido", content = @Content)
    })
    public ResponseEntity<?> getAppUserByUsername(
            @Parameter(description = "Nome de usuário do usuário", required = true)
            @PathVariable String username) {
        AppUser user = appUserService.getAppUserByUsername(username);
        if (user == null) throw new EntityNotFoundException("Nenhum usuário encontrado.");

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/get-by-email/{email}")
    @Operation(summary = "Obter usuário por email", description = "Retorna um usuário baseado no seu email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado", content = @Content(schema = @Schema(implementation = AppUser.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Argumento inválido", content = @Content)
    })
    public ResponseEntity<?> getAppUserByEmail(
            @Parameter(description = "Email do usuário", required = true)
            @PathVariable String email) {
        AppUser user = appUserService.getAppUserByEmail(email);
        if (user == null) throw new EntityNotFoundException("Nenhum usuário encontrado.");

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/get-by-plan/{id}")
    @Operation(summary = "Obter usuários por plano", description = "Retorna uma lista de usuários associados a um plano específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários encontrada", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AppUser.class)))),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "ID do plano inválido", content = @Content)
    })
    public ResponseEntity<?> getAppUsersByPlanId(
            @Parameter(description = "ID do plano", required = true)
            @PathVariable Long id) {
        List<AppUser> users = appUserService.getAppUsersByPlanId(id);
        if (!users.isEmpty()) return ResponseEntity.status(HttpStatus.OK).body(users);

        throw new EntityNotFoundException("Nenhum usuário encontrado.");
    }

    @GetMapping("/get-by-user-role/{id}")
    @Operation(summary = "Obter usuários por função", description = "Retorna uma lista de usuários associados a uma função específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários encontrada", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AppUser.class)))),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "ID da função inválido", content = @Content)
    })
    public ResponseEntity<?> getAppUsersByUserRoleId(
            @Parameter(description = "ID da função do usuário", required = true)
            @PathVariable Long id) {
        List<AppUser> users = appUserService.getAppUsersByUserRoleId(id);
        if (!users.isEmpty()) return ResponseEntity.status(HttpStatus.OK).body(users);

        throw new EntityNotFoundException("Nenhum usuário encontrado.");
    }

    @GetMapping("/leaderboard")
    @Operation(summary = "Obter leaderboard", description = "Retorna uma lista de usuários ordenados por pontuação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários encontrada", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AppUser.class)))),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro na requisição", content = @Content)
    })
    public ResponseEntity<?> getLeaderboard() {
        List<AppUser> users = appUserService.getLeaderboard();
        if (!users.isEmpty()) return ResponseEntity.status(HttpStatus.OK).body(users);

        throw new EntityNotFoundException("Nenhum usuário encontrado.");
    }

    @PostMapping("/insert")
    @Operation(summary = "Inserir um novo usuário", description = "Cria um novo usuário no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso", content = @Content(schema = @Schema(implementation = AppUser.class))),
            @ApiResponse(responseCode = "409", description = "Conflito - Usuário com nome já existente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    public ResponseEntity<?> insertAppUser(
            @Parameter(description = "Dados do novo usuário", required = true)
            @Valid @RequestBody AppUser appUser) {
        try {
            appUser.setName(appUser.getName().strip().toUpperCase());
            appUser.setPassword(new BCryptPasswordEncoder().encode(appUser.getPassword()));
            AppUser savedAppUser = appUserService.saveAppUser(appUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAppUser);
        } catch (DataIntegrityViolationException dive) {
            throw new DataIntegrityViolationException("Usuário com esse nome já existente.");
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Deletar um usuário", description = "Remove um usuário do sistema baseado no ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito - Existem dependências para esse usuário", content = @Content),
            @ApiResponse(responseCode = "400", description = "ID inválido", content = @Content)
    })
    public ResponseEntity<String> deleteAppUser(
            @Parameter(description = "ID do usuário a ser deletado", required = true)
            @PathVariable Long id) {
        try {
            if (appUserService.deleteAppUserById(id) == null) throw new EntityNotFoundException("Usuário não encontrado.");
            return ResponseEntity.status(HttpStatus.OK).body("Usuário deletado com sucesso.");
        } catch (DataIntegrityViolationException dive) {
            throw new DataIntegrityViolationException("Existem dependências para esse usuário. Mude-as para excluir esse usuário.");
        }
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Atualizar um usuário", description = "Atualiza todos os dados de um usuário baseado no ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso", content = @Content(schema = @Schema(implementation = AppUser.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    public ResponseEntity<?> updateAppUser(
            @Parameter(description = "ID do usuário a ser atualizado", required = true)
            @PathVariable Long id,
            @Parameter(description = "Novos dados do usuário", required = true)
            @Valid @RequestBody AppUser appUser) {
        if (appUserService.getAppUserById(id) == null) throw new EntityNotFoundException("Usuário não encontrado.");

        appUser.setId(id);
        appUser.setName(appUser.getName().strip().toUpperCase());
        appUser.setPassword(new BCryptPasswordEncoder().encode(appUser.getPassword()));
        appUser.setUpdatedAt(LocalDateTime.now());

        appUserService.saveAppUser(appUser);
        return ResponseEntity.status(HttpStatus.OK).body(appUser);
    }

    @PatchMapping("/update/{id}")
    @Operation(summary = "Atualizar parcialmente um usuário", description = "Atualiza alguns dados de um usuário baseado no ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso", content = @Content(schema = @Schema(implementation = AppUser.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content)
    })
    public ResponseEntity<?> updateAppUserPartially(
            @Parameter(description = "ID do usuário a ser atualizado", required = true)
            @PathVariable Long id,
            @Parameter(description = "Campos a serem atualizados", required = true)
            @RequestBody Map<String, Object> updates) {
        AppUser existingAppUser = appUserService.getAppUserById(id);  // validando se existe
        if (existingAppUser == null) throw new EntityNotFoundException("Usuário não encontrado.");

        existingAppUser.setId(id);
        updates.forEach((key, value) -> {
            try {
                switch (key) {
                    case "name" -> existingAppUser.setName(((String) value).strip().toUpperCase());
                    case "username" -> existingAppUser.setUsername((String) value);
                    case "password" -> {
                        String password = new BCryptPasswordEncoder().encode((String) value);
                        existingAppUser.setPassword(password);
                    }
                    case "level" -> existingAppUser.setLevel((Integer) value);
                    case "points" -> existingAppUser.setPoints((Integer) value);
                    case "coins" -> existingAppUser.setCoins((Integer) value);
                    case "birthDate" -> existingAppUser.setBirthDate((LocalDate) value);
                    case "pictureUrl" -> existingAppUser.setPictureUrl((String) value);
                    case "plan" -> existingAppUser.setPlan((Plan) value);
                    case "userRole" -> existingAppUser.setUserRole((UserRole) value);
                    default -> throw new IllegalArgumentException("Campo " + key + " não é atualizável.");
                }
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Valor inválido para o campo " + key + ": " + e.getMessage(), e);
            }
        });

        // validando se algum campo está incorreto
        Map<String, String> errors = ControllerUtils.verifyObject(existingAppUser, new ArrayList<>(updates.keySet()));
        if (!errors.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);

        existingAppUser.setUpdatedAt(LocalDateTime.now());
        appUserService.saveAppUser(existingAppUser);
        return ResponseEntity.status(HttpStatus.OK).body("Usuário atualizado com sucesso.");
    }
}
