package managePost

import ManagePostDTO
import SocialRepository
import bitmap
import decompose.DefaultCoroutineExecutor
import koin.Inject
import kotlinx.coroutines.launch
import managePost.ManagePostStore.Intent
import managePost.ManagePostStore.Label
import managePost.ManagePostStore.State
import managePost.ManagePostStore.Message
import toByteArray

class ManagePostExecutor(
    private val socialRepository: SocialRepository = Inject.instance(),
    private val output: (ManagePostComponent.Output) -> Unit
) : DefaultCoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.AddTag -> dispatch(Message.TagsChanged(state().pickedTags+intent.tag))
            is Intent.DeleteTag -> dispatch(Message.TagsChanged(state().pickedTags-intent.tag))
            is Intent.AddImage -> addImage(intent.byteArray)
            is Intent.DeleteImage -> deleteImage(intent.imageId)
            Intent.Save -> save()
        }
    }

    private fun save() {
        scope.launch {
            val isForCreating = state().id == null
            if (isForCreating) socialRepository.insertPost(
                managePostDTO = ManagePostDTO(
                    images = state().images.map { it.toByteArray() },
                    tags = state().pickedTags,
                    text = state().text,
                    creationDate = null,
                    creationTime = null,
                    newsData = state().newsData,
                    id = null
                )
            )

        }.invokeOnCompletion {
            output(ManagePostComponent.Output.NavigateToFeed)
        }
    }


    private fun deleteImage(imageId: Int) {
        scope.launch {
            val newList = state().images.toMutableList()
            newList.removeAt(index = imageId)
            dispatch(Message.ImagesChanged(newList))
        }
    }

    private fun addImage(byteArray: ByteArray) {
        scope.launch {
            byteArray.bitmap?.let {
                dispatch(Message.ImagesChanged(state().images + it))
            }
        }
    }
}
