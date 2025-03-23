package nl.bluevoid.githubexplorer.presentation.ui

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage

@Composable
fun AvatarImage(imageUrl: String, size: Dp) {
    AsyncImage(
        model = imageUrl,
        contentDescription = "Avator image",
        modifier = Modifier.size(size)
    )
}