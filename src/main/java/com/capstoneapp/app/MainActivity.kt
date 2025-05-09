package com.capstoneapp.app

import android.os.Build
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstoneapp.app.ThemeViewModel
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.capstoneapp.app.ui.theme.CapstoneAppTheme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            val isDarkTheme by themeViewModel.isDarkTheme
            val navController = rememberNavController()

            // State for Drawer
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val scope = rememberCoroutineScope()

            CapstoneAppTheme(darkTheme = isDarkTheme) {
                // ModalNavigationDrawer wraps the entire Scaffold
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            Text("Menu", modifier = Modifier.padding(16.dp), fontSize = 30.sp , fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)

                            if (currentRoute != null) {
                                DrawerItem("Home", "home", currentRoute) {
                                    navController.navigate("home")
                                    scope.launch { drawerState.close() }
                                }
                            }
                            if (currentRoute != null) {
                                DrawerItem("Settings", "settings", currentRoute) {
                                    navController.navigate("settings")
                                    scope.launch { drawerState.close() }
                                }
                            }
                            if (currentRoute != null) {
                                DrawerItem("User Manual", "user_manual", currentRoute) {
                                    navController.navigate("user_manual")
                                    scope.launch { drawerState.close() }
                                }
                            }
                            if (currentRoute != null) {
                                DrawerItem("Members", "members", currentRoute) {
                                    navController.navigate("members")
                                    scope.launch { drawerState.close() }
                                }
                            }
                        }
                    }
                ) {
                    // Scaffold content
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Text(
                                        "TrackCare",
                                        fontSize = 25.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontFamily = FontFamily.Serif,
                                        letterSpacing = 0.3.sp
                                    )
                                },
                                navigationIcon = {
                                    // Hamburger Menu Icon
                                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                                    }
                                },
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor =  MaterialTheme.colorScheme.surface

                                )
                            )
                        }
                    ) { innerPadding ->
                        // Navigation setup with NavHost
                        NavHost(navController, startDestination = "home") {
                            composable("home") {
                                HomeScreen(navController)
                            }
                            composable("camera") { CameraScreen(navController) }
                            composable("light") { LightScreen(navController) }
                            composable("temperature") { TemperatureScreen(navController) }
                            composable("user_manual") { UserManualScreen(navController) }
                            composable("members") { MembersScreen(navController) }
                            composable("attendance") { AttendanceScreen(navController)}
                            composable("settings") { SettingsScreen(navController, themeViewModel)}



                            // Define routes for each member's details screen
                            composable("member_james_keith") { JamesKeithScreen(navController) }
                            composable("member_rhina") { RhinaScreen(navController) }
                            composable("member_christopher") { ChristopherScreen(navController) }
                            composable("member_elijah") { ElijahScreen(navController) }
                            composable("member_allen") { AllenScreen(navController) }
                            composable("member_ryle") { RyleScreen(navController) }
                            // Add more routes for other members
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerItem(label: String, route: String, currentRoute: String, onClick: () -> Unit) {
    val isSelected = currentRoute == route
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent
    val textColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 25.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    val activityColor = MaterialTheme.colorScheme.surface
    val backgroundColor = MaterialTheme.colorScheme.background
    val activities = listOf("Camera", "Light", "Temperature", "Attendance")

    Box(Modifier.fillMaxSize().background(backgroundColor)) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(top = 120.dp, start = 25.dp, end = 25.dp)
        ) {
            items(activities) { activity ->
                ActivityCard(name = activity, backgroundColor = activityColor) {
                    navController.navigate(
                        when (activity) {
                            "Camera" -> "camera"
                            "Light" -> "light"
                            "Temperature" -> "temperature"
                            "User Manual" -> "user_manual"
                            "Members" -> "members"
                            "Attendance" -> "attendance"
                            "Settings" -> "settings"
                            else -> ""
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ActivityCard(name: String, backgroundColor: Color, onClick: () -> Unit) {
    val iconResId = when (name) {
        "Camera" -> R.drawable.baseline_camera
        "Light" -> R.drawable.baseline_flashlight_on_24
        "Temperature" -> R.drawable.baseline_device_thermostat_24
        "Attendance" -> R.drawable.baseline_person_24
        else -> R.drawable.baseline_admin_panel_settings_24
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = "$name Icon",
                modifier = Modifier.size(30.dp),
                tint = Color.Black
            )
            Spacer(Modifier.width(20.dp))
            Text(name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}


@Composable
fun CameraScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Text("Camera Screen", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Text("Camera 1\n\n\n\n\n\n\n\n\n\nCamera 2")
        Spacer(modifier = Modifier.weight(1f)) // Replaces hard-coded Spacer height
        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }
    }
}

@Composable
fun LightScreen(navController: NavController) {
    // State to track whether the light is on or off
    var isLightOn by remember { mutableStateOf(false) }

    // Function to toggle the light statez
    val toggleLight = {
        isLightOn = !isLightOn
    }

    // Main screen UI
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Text("Lighting Status", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        // Display the light status (on or off)
        Spacer(modifier = Modifier.height(150.dp))//pagitan mula sa taas
        Text(
            text = if (isLightOn) "Light is ON" else "Light is OFF",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp)) //pagitan ng indicator at pic

        // Display a corresponding icon based on the light state
        if (isLightOn) {
            Image(
                painter = painterResource(id = R.drawable.baseline_flashlight_on_24), // Light ON icon
                contentDescription = "Light ON",
                modifier = Modifier.size(100.dp)//size ng image
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.baseline_flashlight_off_24), // Light OFF icon
                contentDescription = "Light OFF",
                modifier = Modifier.size(100.dp)//size ng image
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Button to toggle the light state
        Button(onClick = toggleLight) {
            Text(text = if (isLightOn) "Turn Off" else "Turn On", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(225.dp))//spacer ng back button

        // Back Button to navigate to home
        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }
    }
}

@Composable
fun TemperatureScreen(navController: NavController) {
    var temperature by remember { mutableStateOf(25f) } // Placeholder, replace with actual sensor data

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Temperature Screen", fontSize = 30.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        TemperatureGauge(temperature) // ⬅️ Adds the Temperature Gauge UI

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Temperature Reading: $temperature°C",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = when (temperature) {
                in 0f..25f -> "❄ Cold Temperature"
                in 26f..37f -> "🌡 Normal Temperature"
                else -> "🔥 Hot Temperature"
            },
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = when (temperature) {
                in 0f..25f -> Color.Blue
                in 26f..37f -> Color.Green
                else -> Color.Red
            }
        )


        Spacer(modifier = Modifier.height(20.dp))

        // Simulate temperature change (replace this with actual sensor data)
        Button(onClick = { temperature = (10..40).random().toFloat() }) {
            Text("Random Temp")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }
    }
}

@Composable
fun TemperatureGauge(temperature: Float) {
    // Define the correct color order
    val gaugeColor = listOf(
        Color.Blue, Color.Blue, // Cold (0-24°C)
        Color.Green, Color.Green, // Normal (25-37°C)
        Color.Red, Color.Red // Hot (38+°C)
    )

    val rotation = remember { Animatable(-90f) }

    LaunchedEffect(temperature) {
        val newRotation = when (temperature) {
            in 0f..24f -> -90f  // Cold (Left)
            in 25f..37f -> 0f    // Normal (Middle)
            else -> 90f          // Hot (Right)
        }

        rotation.animateTo(newRotation, animationSpec = tween(1000))
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val arcRadius = size.minDimension / 2
            val startAngle = -120f
            val sweepAngle = 240f
            val sections = 6
            val sectionSweep = sweepAngle / sections

            gaugeColor.forEachIndexed { index, color ->
                drawArc(
                    color = color,
                    startAngle = startAngle + index * sectionSweep,
                    sweepAngle = sectionSweep,
                    useCenter = false,
                    style = Stroke(width = 40f, cap = StrokeCap.Round), //adjust ng width ng UI
                    size = Size(arcRadius * 2, arcRadius * 2),
                    topLeft = Offset(size.width / 2 - arcRadius, size.height / 2 - arcRadius)
                )
            }

            // Gauge Needle (Pointer)
            drawLine(
                color = Color.Black,
                start = Offset(size.width / 2, size.height / 2),
                end = Offset(
                    (size.width / 2) + arcRadius * cos(rotation.value.toRadians()),
                    (size.height / 2) + arcRadius * sin(rotation.value.toRadians())
                ),
                strokeWidth = 25f, //kamay ng UI arrow ganern
                cap = StrokeCap.Round
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Temperature Label
        Text(
            text = when (temperature) {
                in 0f..24f -> "❄ Cold"
                in 25f..37f -> "🌡 Normal"
                else -> "🔥 Hot"
            },
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = when (temperature) {
                in 0f..24f -> Color.Blue
                in 25f..37f -> Color.Green
                else -> Color.Red
            }
        )
    }
}

// Helper function to convert degrees to radians
fun Float.toRadians(): Float = (this * PI / 180).toFloat()


@Composable
fun UserManualScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "\n\n\n\n📖 TrackCare User Manual",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Scrollable Content
        Box(
            modifier = Modifier
                .weight(1f) // Ensures the scrolling section gets proper space
                .verticalScroll(rememberScrollState()) // Enable scrolling
        ) {
            Column {
                Text(
                    text = "Welcome to TrackCare! This app assists caregivers and family members in monitoring elderly patients efficiently.\n",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )

                // CAMERA FEATURE
                Text(
                    text = "🔍 Main Features: \n\n 📷 1. Camera",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text =  "     ‣ Capture images of patients or their\n " +
                            "       environment.\n" +
                            "     ‣ Equipped with AI Facial Recognition\n\n " +
                            "        How to use:\n" +
                            "     1. Tap ✶Camera✶ on the main menu.\n" +
                            "     2. Grant Camera permissions if prompted.\n" +
                            "     3. The Camera's view will be visible to you.\n" +
                            "     4. The camera provides a live view, and \n " +
                            "        AI facial recognition enables identification\n " +
                            "        of the person.\n ",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start
                )

                // LIGHT FEATURE
                Text(
                    text = "💡 2. Automatic Lighting",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text =  "      ‣ Automatic Lighting Technology.\n " +
                            "     ‣ Turns the Lights on and off.\n\n " +
                            "        How to use:\n" +
                            "     1. Tap ✶Light✶ on the main menu.\n" +
                            "     2. The Light will turn ON.\n" +
                            "     3. Tap again to turn OFF.\n" +
                            "     4. Ensure that the app has permission to\n " +
                            "        access the Light.\n\n",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start
                )

                //Temperature Monitoring
                Text(
                    text = "\uD83C\uDF21 3. Temperature Monitoring\n ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text =  "      ‣ Displays real-time temperature data from an\n " +
                            "        external sensor (if available). \n " +
                            "        How to use:\n " +
                            "     1. Tap ✶Temperature✶ on the main menu.\n " +
                            "     2. Wait for the app to retrieve temperature\n " +
                            "         data.\n "+
                            "     3. Ensure that the sensor is properly\n " +
                            "         connected.\n "
                )

                //Members
                Text(
                    text = "📘 4. User Manual\n ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text =  "      ‣ View registered members in the system.\n " +
                            "        How to use:\n " +
                            "     1. Tap ✶Members✶ on the main menu.\n " +
                            "     2. A list of registered members will appear.\n\n"
                )

                //Troubleshooting
                Text(
                    text = "⚠ Troubleshooting & Common Issues\n ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text (
                    text =  " \uD83D\uDCF7 Camera not working?\n " +
                            "     ‣ Ensure the app has permission to access\n " +
                            "        the camera.\n " +
                            "    ‣ Restart the app and try again.\n " +
                            "    ‣ Check if another app is using the camera.\n\n " +

                            " \uD83D\uDCA1 Flashlight not turning on?\n " +
                            "    ‣ Ensure the flashlight is not being used by\n " +
                            "     another app.\n " +
                            "    ‣ Restart your phone and try again.\n\n " +

                            " \uD83C\uDF21 Temperature not displaying?\n " +
                            "    ‣ Check if the sensor is properly connected.\n " +
                            "    ‣ Ensure the app has permission to access \n " +
                            "       external sensors.\n\n " +

                            " 📞  Need Assistance?\n " +
                            "       If you need help, contact us:\n" +
                            " 📧  Email: MPCDebugDynamo@gmail.com\n"

                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(bottom = 5.dp) // Prevents overlap with system navigation
                .windowInsetsPadding(WindowInsets.navigationBars) // Ensures space for system buttons
        ) {
            Text("Back")
        }
    }
}

@Composable
fun SettingsScreen(navController: NavController, themeViewModel: ThemeViewModel) {
    var notificationsEnabled by remember { mutableStateOf(false) }
    val isDarkTheme by themeViewModel.isDarkTheme

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        notificationsEnabled = isGranted
        if (!isGranted) {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp, vertical = 60.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // 🔔 Enable Notifications Switch
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Enable Notifications",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        notificationsEnabled = it
                        if (it) {
                            Toast.makeText(context, "Notifications enabled", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Notifications disabled", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                    checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                    uncheckedTrackColor = MaterialTheme.colorScheme.outlineVariant
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Dark Mode",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { themeViewModel.toggleTheme() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                    checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                    uncheckedTrackColor = MaterialTheme.colorScheme.outlineVariant
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 👤 Account Info button
        Button(
            onClick = { /* TODO */ },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Account Info", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}




@Composable
fun AttendanceScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Attendance Page")
    }
}


@Composable
fun MembersScreen(navController: NavController) {
    data class Member(val name: String, val imageRes: Int)

    val members = listOf(
        Member("James Keith Lamanilao", R.drawable.keith),
        Member("Rhina Creer Lubino", R.drawable.rhina),
        Member("Christopher Robante", R.drawable.topher),
        Member("Elijah Tenorio", R.drawable.jack),
        Member("Allen Madison Verano", R.drawable.madi),
        Member("Ryle Andrei Vergara", R.drawable.ryle)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp) ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))

        Text("Members Screen", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight(0.8f)
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(members) { member ->
                MemberCard(name = member.name, imageRes = member.imageRes) {
                    val route = when (member.name) {
                        "James Keith Lamanilao" -> "member_james_keith"
                        "Rhina Creer Lubino" -> "member_rhina"
                        "Christopher Robante" -> "member_christopher"
                        "Elijah Tenorio" -> "member_elijah"
                        "Allen Madison Verano" -> "member_allen"
                        "Ryle Andrei Vergara" -> "member_ryle"
                        else -> null
                    }

                    route?.let { navController.navigate(it) }

                }
            }
        }

        // Back Button
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }
    }
}

@Composable
fun MemberCard(name: String, imageRes: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

        ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "$name Icon",
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = name,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f)
                    .weight(1f)
                    .wrapContentHeight(Alignment.CenterVertically),
                textAlign = TextAlign.Center
            )
        }
    }
}

//new window per member
@Composable
fun JamesKeithScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(100.dp))
        Image(
            painter = painterResource(id = R.drawable.keith),
            contentDescription = "Profile Picture of James Keith Lamanilao",
            modifier = Modifier
                .size(150.dp) // Set size
                .clip(CircleShape) // Make it circular
                .border(2.dp, Color.Gray, CircleShape) // Add border
        )

        Text("\nJames Keith Lamanilao", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)

        Spacer(modifier = Modifier.height(20.dp))//height nung TRACKCARE BETWEEN DETAILS ABOUT JAMES KEITH

        Text(text = "Bachelor of Science in Electronics Engineering\n", fontSize = 20.sp, fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "\nEducational Attainment\n",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Text("College:", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().padding(start = 20.dp))
        Text(
            text = "Marikina Polytechnic College (MPC) \n"+
                    "2021 - Present \n",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text("Senior High School:", fontSize = 20.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            text =  "Golden Faith Academy(GFA) \n"+
                    "2019 - 2021 \n",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text("Junior High School:", fontSize = 20.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp))
        Text(
            text =  "Dela Paz National High School(DPNHS) \n" +
                    "2017 - 2019 \n" +
                    "Tagum City National Comprehensive High School(TCNCHS) \n"+
                    "2015 - 2017 \n",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text("Elementary School:", fontSize = 20.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp))
        Text(
            text = "Sampaloc Site II Elementary School \n"+
                    "2014 - 2015",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            text = "Juan Sumulong Elementary School \n"+
                    "2009 - 2013 ",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun RhinaScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Image(
            painter = painterResource(id = R.drawable.rhina),
            contentDescription = "Profile Picture of Rhina C. Lubino",
            modifier = Modifier
                .size(150.dp) // Set size
                .clip(CircleShape) // Make it circular
                .border(2.dp, Color.Gray, CircleShape) // Add border
        )
        Text ("\nRhina Creer Lubino", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)

        Spacer(modifier = Modifier.height(20.dp))//height nung TRACKCARE BETWEEN DETAILS ABOUT RHINA CREER LUBINO

        Text(
            text = "Bachelor of Science in Electronics Engineering\n",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "\nEducational Attainment\n",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            "College:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp)
        )
        Text(
            text = "Marikina Polytechnic College \n" +
                    "2021 - Present \n",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            "Senior High School:", fontSize = 20.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            text = "Silangan National High School (SNHS) \n" +
                    "2019 - 2021 \n",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            "Junior High School:", fontSize = 20.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            text = "Silangan National High School (SNHS) \n" +
                    "2015 - 2019 \n",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            "Elementary School:", fontSize = 20.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            text = "Sto. Niño Elementary School (SNES) \n" +
                    "2009 - 2015",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))
    }
}
@Composable
fun ChristopherScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Image(
            painter = painterResource(id = R.drawable.topher),
            contentDescription = "Profile Picture of Christopher Robante",
            modifier = Modifier
                .size(150.dp) // Set size
                .clip(CircleShape) // Make it circular
                .border(2.dp, Color.Gray, CircleShape) // Add border
        )
        Text ("\nChristopher Robante", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)

        Spacer(modifier = Modifier.height(20.dp))//height nung TRACKCARE BETWEEN DETAILS ABOUT CHRISTOPHER ROBANTE
        Text(
            text = "Bachelor of Science in Electronics Engineering\n",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "\nEducational Attainment\n",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            "College:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp)
        )
        Text(
            text = "Marikina Polytechnic College \n" +
                    "2021 - Present \n",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            "Senior High School:", fontSize = 20.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            text =  "Golden Faith Academy(GFA) \n"+
                    "2019 - 2021 \n",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            "Junior High School:", fontSize = 20.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            text = "Casimiro A. Ynares Sr. Memorial National High School (CAYSMNHS) \n" +
                    "2015 - 2019 \n",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            "Elementary School:", fontSize = 20.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            text = "Corazon A. Aquino Elementary School (CCAES) \n" +
                    "2009 - 2015",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun ElijahScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Image(
            painter = painterResource(id = R.drawable.jack),
            contentDescription = "Profile Picture of Elijah Tenorio",
            modifier = Modifier
                .size(150.dp) // Set size
                .clip(CircleShape) // Make it circular
                .border(2.dp, Color.Gray, CircleShape) // Add border
        )
        Text("\nElijah Tenorio", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)

        Spacer(modifier = Modifier.height(20.dp))//height nung TRACKCARE BETWEEN DETAILS ABOUT ELIJAH TENORIO
        Text(
            text = "Bachelor of Science in Electronics Engineering\n",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "\nEducational Attainment\n",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            "College:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp)
        )
        Text(
            text = "Marikina Polytechnic College \n" +
                    "2021 - Present \n",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            "Senior High School:", fontSize = 20.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            text = "Mayamot National High School (MNHS) \n" +
                    "2019 - 2021 \n",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            "Junior High School:", fontSize = 20.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            text = "Mayamot National High School (MNHS) \n" +
                    "2015 - 2019 \n",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            "Elementary School:", fontSize = 20.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            text = "Mayamot Elementary School (MES) \n" +
                    "2009 - 2015",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun AllenScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Image(
            painter = painterResource(id = R.drawable.madi),
            contentDescription = "Profile Picture of Allen Madison Verano",
            modifier = Modifier
                .size(150.dp) // Set size
                .clip(CircleShape) // Make it circular
                .border(2.dp, Color.Gray, CircleShape) // Add border
        )
        Text("\nAllen Madison Verano", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)

        Spacer(modifier = Modifier.height(20.dp))//height nung TRACKCARE BETWEEN DETAILS ABOUT ALLEN MADISON VERANO
        Text(
            text = "Bachelor of Science in Electronics Engineering\n",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "\nEducational Attainment\n",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            "College:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp)
        )
        Text(
            text = "Marikina Polytechnic College \n" +
                    "2021 - Present \n",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            "Senior High School:", fontSize = 20.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            text = "Golden Faith Academy(GFA) \n" +
                    "2019 - 2021 \n",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            "Junior High School:", fontSize = 20.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            text = "Casimiro A. Ynares Sr. Memorial National High School (CAYSMNHS) \n" +
                    "2015 - 2019 \n",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            "Elementary School:", fontSize = 20.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            text = "Muzon Elementary School (MES) \n" +
                    "2009 - 2015",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun RyleScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Image(
            painter = painterResource(id = R.drawable.ryle),
            contentDescription = "Profile Picture of Ryle Andrei Vergara",
            modifier = Modifier
                .size(150.dp) // Set size
                .clip(CircleShape) // Make it circular
                .border(2.dp, Color.Gray, CircleShape) // Add border
        )
        Text("\nAllen Madison Verano", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)

        Spacer(modifier = Modifier.height(20.dp))//height nung TRACKCARE BETWEEN DETAILS ABOUT RYLE ANDREI VERGARA
        Text(
            text = "Bachelor of Science in Electronics Engineering\n",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "\nEducational Attainment\n",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            "College:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp)
        )
        Text(
            text = "Marikina Polytechnic College (MPC)\n" +
                    "2021 - Present \n",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            "Senior High School:", fontSize = 20.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            text = "DEE HWA LIONG ACADEMY \n" +
                    "2019 - 2021 \n",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            "Junior High School:", fontSize = 20.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            text = "Jesus De La Peña National High School \n" +
                    "2015 - 2019 \n",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            "Elementary School:", fontSize = 20.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Text(
            text = "Leodegario Victorino Elementary School (MES) \n" +
                    "2009 - 2015",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))
    }
}

