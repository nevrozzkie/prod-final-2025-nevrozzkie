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
    val createdDate: String,
    val plannedDate: String,
    val completedDate: String?
)
data class Transaction(
    val id: Long,
    val goalId: Long,
    val amount: Long,
    val comment: String,
    val createdDate: String
)