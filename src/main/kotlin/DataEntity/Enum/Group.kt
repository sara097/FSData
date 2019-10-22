package DataEntity.Enum

enum class Group(val label: String) {
    CONTROL("Kontrolna"),
    PD("Badawcza");

    fun findByLabel(label: String) = when (label) {
        CONTROL.label -> CONTROL
        else -> PD
    }
}