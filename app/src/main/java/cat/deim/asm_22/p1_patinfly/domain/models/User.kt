package cat.deim.asm_22.p1_patinfly.domain.models

import java.util.UUID

data class User(
    val uuid: UUID,
    val name: String,
    val email: String,
    val hashedPassword: String,
    val creationDate: String,
    val lastConnection: String,
    val deviceId: String
)