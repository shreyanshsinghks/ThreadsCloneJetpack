package com.hello.threads.itemView

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.hello.threads.R
import com.hello.threads.model.ThreadModel
import com.hello.threads.model.UserModel
import com.hello.threads.navigation.Routes
import com.hello.threads.utils.SharedPref

@Composable
fun UserItem(
    users: UserModel,
    navController: NavHostController
) {
    Column {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    val routesWithData = Routes.OtherUsers.routes.replace("{data}", users.uid)
                    navController.navigate(routesWithData)
                }
        ) {
            val (userImage, userName, date, time, title, image) = createRefs()

            Image(
                painter = rememberAsyncImagePainter(model = users.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(userImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .size(36.dp)
                    .clip(CircleShape), contentScale = ContentScale.Crop
            )

            Text(
                text = users.userName,
                fontSize = 20.sp,
                modifier = Modifier.constrainAs(userName) {
                    top.linkTo(userImage.top)
                    start.linkTo(userImage.end, margin = 8.dp)
                }
            )


            Text(
                text = users.name,
                fontSize = 18.sp,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(userName.bottom, margin = 4.dp)
                    start.linkTo(userName.start)
                }
            )

        }

        Divider(color = Color.LightGray, thickness = 1.dp)
    }

}


