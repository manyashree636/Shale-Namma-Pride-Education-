package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.myapplication.ui.theme.*
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                var showSplash by remember { mutableStateOf(true) }
                LaunchedEffect(Unit) {
                    delay(2000)
                    showSplash = false
                }
                
                if (showSplash) {
                    SplashScreen()
                } else {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(DeepBlue, Crimson))),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                modifier = Modifier.size(140.dp),
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.2f),
                border = BorderStroke(3.dp, Gold)
            ) {
                Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.padding(32.dp), tint = Gold)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Shale Namma Pride", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Text("Empowering Government Schools", fontSize = 18.sp, color = Color.White.copy(alpha = 0.8f))
            Spacer(modifier = Modifier.height(48.dp))
            CircularProgressIndicator(color = Gold, strokeWidth = 4.dp)
        }
    }
}

sealed class Screen(val label: String, val icon: ImageVector) {
    object Home : Screen("Home", Icons.Default.Home)
    object Meal : Screen("Meal", Icons.Default.Restaurant)
    object Gallery : Screen("Facilities", Icons.Default.Collections)
     object Feedback : Screen("Feedback", Icons.Default.Email)
    object Profile : Screen("About", Icons.Default.Info)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var isKannada by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth().statusBarsPadding(),
                color = DeepBlue.copy(alpha = 0.85f),
                border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.2f))
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            if (isKannada) "ಶಾಲೆ ನಮ್ಮ ಹೆಮ್ಮೆ" else "Shale Namma Pride",
                            fontWeight = FontWeight.Bold, color = Color.White, fontSize = 20.sp
                        )
                    },
                    actions = {
                        TextButton(onClick = { isKannada = !isKannada }) {
                            Text(if (isKannada) "English" else "ಕನ್ನಡ", color = Gold, fontWeight = FontWeight.Bold)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White.copy(alpha = 0.9f),
                tonalElevation = 8.dp
            ) {
                val items = listOf(Screen.Home, Screen.Meal, Screen.Gallery, Screen.Feedback, Screen.Profile)
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label, fontSize = 10.sp) },
                        selected = selectedScreen == screen,
                        onClick = { selectedScreen = screen },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Crimson, selectedTextColor = Crimson,
                            unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray,
                            indicatorColor = LightBlue
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFE3F2FD), Color.White)))
        ) {
            BackgroundBlobs()
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                AnimatedContent(
                    targetState = selectedScreen,
                    transitionSpec = { fadeIn(tween(500)) togetherWith fadeOut(tween(500)) },
                    label = "ScreenTransition"
                ) { screen ->
                    when (screen) {
                        Screen.Home -> HomeScreen(isKannada)
                        Screen.Meal -> MealUpdateScreen(isKannada)
                        Screen.Gallery -> FacilityGalleryScreen(isKannada)
                        Screen.Feedback -> FeedbackScreen(isKannada)
                        Screen.Profile -> AboutSchoolScreen(isKannada)
                    }
                }
            }
        }
    }
}

@Composable
fun BackgroundBlobs() {
    val infiniteTransition = rememberInfiniteTransition(label = "blobs")
    val blobOffset by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 50f,
        animationSpec = infiniteRepeatable(tween(5000, easing = LinearEasing), RepeatMode.Reverse),
        label = "blobMovement"
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.size(300.dp).offset(x = (-100).dp + blobOffset.dp, y = 50.dp).background(Color(0xFFFFEE58).copy(alpha = 0.2f), CircleShape).blur(80.dp))
        Box(modifier = Modifier.size(350.dp).align(Alignment.BottomEnd).offset(x = 100.dp, y = (100).dp - blobOffset.dp).background(Color(0xFF2ECC71).copy(alpha = 0.15f), CircleShape).blur(100.dp))
    }
}

@Composable
fun HomeScreen(isKannada: Boolean) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
    ) {
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp), elevation = CardDefaults.cardElevation(12.dp)) {
                Box(modifier = Modifier.background(Brush.linearGradient(listOf(Color(0xFF283593), Color(0xFF3F51B5)))).padding(28.dp).fillMaxWidth()) {
                    Column {
                        Text(if (isKannada) "ನಮ್ಮ ಶಾಲೆಗೆ ಸುಸ್ವಾಗತ" else "Welcome to Our School", fontSize = 18.sp, color = Color.White.copy(alpha = 0.9f), fontWeight = FontWeight.Medium)
                        Text(if (isKannada) "ಶಾಲಾ ಪಾರದರ್ಶಕ ಪೋರ್ಟಲ್" else "School Transparency Portal", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                }
            }
        }

        item {
            Text(if (isKannada) "ಶಾಲಾ ಸಂಭ್ರಮದ ಕ್ಷಣಗಳು" else "School Highlights", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DeepBlue)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(listOf("Sports Day", "Science Expo", "Republic Day", "Garden Visit")) { event ->
                    Card(modifier = Modifier.size(width = 200.dp, height = 120.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = DeepBlue.copy(alpha = 0.1f))) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Image, contentDescription = null, tint = DeepBlue)
                                Text(event, fontWeight = FontWeight.Bold, color = DeepBlue)
                            }
                        }
                    }
                }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth(), border = BorderStroke(1.dp, Gold), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Gold, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(if (isKannada) "ವಾರದ ವಿದ್ಯಾರ್ಥಿ" else "Student of the Week", fontWeight = FontWeight.Bold, color = Crimson, fontSize = 14.sp)
                        Text("Rahul Kumar (Grade 10)", fontWeight = FontWeight.ExtraBold, color = DeepBlue, fontSize = 18.sp)
                    }
                }
            }
        }

        item {
            Text(if (isKannada) "ಇತ್ತೀಚಿನ ಅಪ್‌ಡೇಟ್‌ಗಳು" else "Notice Board", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DeepBlue)
        }
        items(listOf(
            if (isKannada) "ಪೋಷಕರ ಸಭೆ - ನಾಳೆ ಸಂಜೆ 4ಕ್ಕೆ" else "PTA Meeting - Tomorrow 4 PM",
            if (isKannada) "ಮುಂದಿನ ವಾರ ದಸರಾ ರಜೆ ಪ್ರಾರಂಭ" else "Dasara Holidays start next week",
            if (isKannada) "ಗಣಿತ ಪರೀಕ್ಷೆ ದಿನಾಂಕ ಪ್ರಕಟ" else "Math Exam Dates Announced"
        )) { update ->
            ListItem(
                headlineContent = { Text(update, fontWeight = FontWeight.SemiBold) }, 
                leadingContent = { Icon(Icons.Default.Campaign, contentDescription = null, tint = Crimson) }, 
                modifier = Modifier.background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            )
        }
    }
}

@Composable
fun MealUpdateScreen(isKannada: Boolean) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(if (isKannada) "ಬಿಸಿ ಊಟದ ವಿವರ" else "Mid-Day Meal Tracker", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = DeepBlue)
        Text(if (isKannada) "ದೈನಂದಿನ ಪಾರದರ್ಶಕತೆಗಾಗಿ ಊಟದ ವರದಿ" else "Daily meal updates for full transparency.", color = Color.Gray)
        
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)), elevation = CardDefaults.cardElevation(4.dp)) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Restaurant, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(if (isKannada) "ಇಂದಿನ ಮೆನು" else "Today's Menu", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32), fontSize = 20.sp)
                        Text("Oct 25, 2023", fontSize = 14.sp)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(if (isKannada) "ಊಟದ ಚಿತ್ರಗಳು" else "Meal Pictures", fontWeight = FontWeight.Bold, color = Color.DarkGray, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(12.dp))
                
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    val mealItems = listOf(
                        Icons.Default.SetMeal to (if (isKannada) "ಬಿಸಿ ಅನ್ನ" else "Steamed Rice"),
                        Icons.Default.SoupKitchen to (if (isKannada) "ಬೇಳೆ ಸಾಂಬಾರ್" else "Dal Sambar"),
                        Icons.Default.BakeryDining to (if (isKannada) "ಮೊಟ್ಟೆ / ಬಾಳೆಹಣ್ಣು" else "Egg / Banana")
                    )
                    items(mealItems) { item ->
                        Card(
                            modifier = Modifier.size(width = 160.dp, height = 180.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .background(LightBlue.copy(alpha = 0.3f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(item.first, contentDescription = null, modifier = Modifier.size(64.dp), tint = SuccessGreen.copy(alpha = 0.6f))
                                }
                                Text(
                                    text = item.second,
                                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = DeepBlue
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(if (isKannada) "ವಿವರಗಳು:" else "Details:", fontWeight = FontWeight.Bold)
                Text("• " + (if (isKannada) "ತಾಜಾ ಅನ್ನ" else "Fresh Steamed Rice"), fontSize = 16.sp)
                Text("• " + (if (isKannada) "ತರಕಾರಿ ಸಾಂಬಾರ್" else "Vegetable Sambar"), fontSize = 16.sp)
                Text("• " + (if (isKannada) "ಬೇಯಿಸಿದ ಮೊಟ್ಟೆ" else "Boiled Egg"), fontSize = 16.sp)
                Text("• " + (if (isKannada) "ಸಿಹಿ ಹಾಲು" else "Hot Milk"), fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun FacilityGalleryScreen(isKannada: Boolean) {
    var selectedPhoto by remember { mutableStateOf<String?>(null) }
    if (selectedPhoto != null) {
        Dialog(onDismissRequest = { selectedPhoto = null }) {
            Card(modifier = Modifier.fillMaxWidth().height(400.dp), shape = RoundedCornerShape(24.dp)) {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(150.dp), tint = Color.Gray)
                        Text(selectedPhoto!!, color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(onClick = { selectedPhoto = null }, colors = ButtonDefaults.buttonColors(containerColor = Crimson)) { Text("Close") }
                    }
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(if (isKannada) "ಶಾಲಾ ಸೌಲಭ್ಯಗಳು" else "Facility Gallery", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = DeepBlue)
        Spacer(modifier = Modifier.height(16.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            val facilities = listOf("Smart Class", "Science Lab", "Library", "Computer Lab", "Playground", "Kitchen")
            items(facilities) { facility ->
                Card(modifier = Modifier.height(140.dp).clickable { selectedPhoto = facility }, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f))) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                        Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(64.dp).align(Alignment.Center), tint = DeepBlue.copy(alpha = 0.1f))
                        Text(facility, modifier = Modifier.fillMaxWidth().background(DeepBlue.copy(alpha = 0.7f)).padding(8.dp), color = Color.White, fontSize = 12.sp, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@Composable
fun FeedbackScreen(isKannada: Boolean) {
    var name by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf("") }
    var done by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(if (isKannada) "ಪೋಷಕರ ಪ್ರತಿಕ್ರಿಯೆ" else "Parent Feedback", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = DeepBlue)
        Text(if (isKannada) "ಮ್ಯಾನೇಜ್‌ಮೆಂಟ್‌ನೊಂದಿಗೆ ನೇರವಾಗಿ ಸಂವಹನ ನಡೆಸಿ." else "Parents can directly communicate with school management.", color = Color.DarkGray)
        
        if (done) {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFC8E6C9))) {
                Text(if (isKannada) "ಸಲ್ಲಿಸಿದ್ದಕ್ಕಾಗಿ ಧನ್ಯವಾದಗಳು!" else "Thank you for your feedback!", modifier = Modifier.padding(16.dp), color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
            }
        }
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text(if (isKannada) "ಹೆಸರು" else "Name (Optional)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = msg, onValueChange = { msg = it }, label = { Text(if (isKannada) "ಸಲಹೆ ಅಥವಾ ದೂರು" else "Feedback / Message") }, modifier = Modifier.fillMaxWidth().height(180.dp))
        Button(onClick = { if (msg.isNotBlank()) done = true }, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = Crimson), shape = RoundedCornerShape(12.dp)) {
            Text(if (isKannada) "ಸಲ್ಲಿಸಿ" else "Submit Feedback")
        }
    }
}

@Composable
fun AboutSchoolScreen(isKannada: Boolean) {
    var admin by remember { mutableStateOf(false) }
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Text(if (isKannada) "ಶಾಲೆಯ ಬಗ್ಗೆ" else "About School", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = DeepBlue)
            Card(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Government Model Higher Primary School", fontWeight = FontWeight.Bold, color = DeepBlue, fontSize = 18.sp)
                    Text(if (isKannada) "ಸ್ಥಾಪನೆ: ೧೯೫೪" else "Established: 1954", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(if (isKannada) "ಗುಣಮಟ್ಟದ ಶಿಕ್ಷಣ ಮತ್ತು ಶಾಲಾ ಆಡಳಿತದಲ್ಲಿ ಪಾರದರ್ಶಕತೆಯನ್ನು ತರುವುದು ನಮ್ಮ ಗುರಿ." else "Our goal is to bring quality education and transparency in school administration.")
                }
            }
        }
        item {
            Button(onClick = { admin = !admin }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = DeepBlue)) {
                Text(if (admin) "Logout Admin" else "Admin Upload Panel (Faculty)")
            }
        }
        if (admin) {
            item {
                Card(modifier = Modifier.fillMaxWidth(), border = BorderStroke(1.dp, Crimson)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Faculty Dashboard", fontWeight = FontWeight.Bold, color = Crimson)
                        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Upload Meal Photo") }
                        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Post New Achievement") }
                        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Update Holiday Notice") }
                    }
                }
            }
        }
    }
}

@Composable
fun AITutor() {
    var query by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFE8EAF6).copy(alpha = 0.6f)).padding(16.dp)) {
        Text("AI Tutor Siri", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = DeepBlue)
        Spacer(modifier = Modifier.weight(1f))
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(modifier = Modifier.size(150.dp), shape = CircleShape, color = Crimson.copy(alpha = 0.15f), shadowElevation = 8.dp) {
                Icon(Icons.Default.Face, contentDescription = null, modifier = Modifier.padding(32.dp), tint = Crimson)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("How can I help you?", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DeepBlue)
            Text("Kannada or English support available.", color = Color.Gray)
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            TextField(value = query, onValueChange = { query = it }, modifier = Modifier.weight(1f), placeholder = { Text("Ask anything...") }, shape = RoundedCornerShape(32.dp), colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent))
            Spacer(modifier = Modifier.width(12.dp))
            FloatingActionButton(onClick = {}, containerColor = Crimson, contentColor = Color.White, shape = CircleShape) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
            }
        }
    }
}
