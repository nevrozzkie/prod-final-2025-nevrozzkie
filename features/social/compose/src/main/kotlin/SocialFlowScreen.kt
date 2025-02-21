import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun SocialFlowScreen(
    component: SocialComponent
) {
    val stack by component.stack.subscribeAsState()
    Children(
        stack = stack,
        animation = predictiveBackAnimation(
            backHandler = component.backHandler,
            fallbackAnimation = stackAnimation(slide() + fade()),
            onBack = component::onBackClicked
        ),
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (val child = it.instance) {
            is SocialComponent.Child.FeedChild -> SocialFeedScreen(child.component)
            is SocialComponent.Child.ManagePostChild -> ManagePostScreen(child.component)
        }
    }
}