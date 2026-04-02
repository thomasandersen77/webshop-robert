package no.robert.webshop.security

/**
 * Annotasjon for å injisere den innloggede User-entiteten i controller-metoder.
 * 
 * Eksempel:
 * ```
 * @GetMapping("/profile")
 * fun getProfile(@CurrentUser user: User): ResponseEntity<ProfileDto> {
 *     return ResponseEntity.ok(ProfileDto.from(user))
 * }
 * ```
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class CurrentUser
