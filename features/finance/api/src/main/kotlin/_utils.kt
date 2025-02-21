import kotlinx.datetime.LocalDate

//@PrimaryKey val id: Long,
//val name: String,
//@ColumnInfo(name = "target_amount") val targetAmount: Long,
//@ColumnInfo(name = "finished_date") val finishedDate: String,
//@ColumnInfo(name = "created_date") val createdDate: String,
//val completed: Boolean,


data class Goal(
    val id: Long,
    val name: String,
    val targetAmount: Long,
    val savedAmount: Long,
    val createdDate: LocalDate,
    val plannedDate: LocalDate?,
    val completedDate: LocalDate?,
    val isEditing: Boolean
)

data class Transaction(
    val id: Long,
    val fromGoalId: Long?,
    val toGoalId: Long?,
    val fromGoal: String,
    val toGoal: String,
    val amount: String,
    val comment: String,
    val createdDate: String,
    val isEditing: Boolean
)