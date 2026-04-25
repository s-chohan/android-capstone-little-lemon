package com.example.littlelemon

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search

@Composable
fun Home(navController: NavHostController, database: AppDatabase) {
    // Retrieve items from database
    val menuItemsRoom by database.menuItemDao().getAll().observeAsState(emptyList())

    // State for Search and Category filtering
    var searchPhrase by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }

    // Logic: Filter items based on search phrase AND selected category
    val menuItems = if (searchPhrase.isNotBlank() || selectedCategory.isNotBlank()) {
        menuItemsRoom.filter {
            val matchesSearch = it.title.contains(searchPhrase, ignoreCase = true)
            val matchesCategory = if (selectedCategory.isBlank()) true else it.category.equals(selectedCategory, ignoreCase = true)
            matchesSearch && matchesCategory
        }
    } else {
        menuItemsRoom
    }

    Column {
        Header(navController)

        // Step 1: Hero Section with Search Bar
        HeroSection(searchPhrase) { searchPhrase = it }

        // Step 2: Menu Breakdown (Categories)
        val categories = menuItemsRoom.map { it.category }.distinct()
        MenuBreakdown(categories, selectedCategory) { category ->
            // If the same category is clicked twice, clear the filter
            selectedCategory = if (selectedCategory == category) "" else category
        }

        MenuItemsList(menuItems)
    }
}

@Composable
fun Header(navController: NavHostController) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.width(50.dp))
        Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Logo", modifier = Modifier.height(40.dp))
        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "Profile",
            modifier = Modifier.size(50.dp).clickable { navController.navigate(Profile.route) }
        )
    }
}

@Composable
fun HeroSection(searchPhrase: String, onSearchChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .background(Color(0xFF495E57))
            .padding(16.dp)
    ) {
        Text("Little Lemon", fontSize = 40.sp, color = Color(0xFFF4CE14), fontWeight = FontWeight.Bold)
        Text("Chicago", fontSize = 24.sp, color = Color.White)
        Row(modifier = Modifier.padding(top = 18.dp)) {
            Text(
                "We are a family-owned Mediterranean restaurant, focused on traditional recipes served with a modern twist",
                color = Color.White, fontSize = 16.sp, modifier = Modifier.weight(1f).padding(end = 8.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.hero_image),
                contentDescription = "Hero Image",
                modifier = Modifier.size(100.dp).clip(MaterialTheme.shapes.medium)
            )
        }

        // Search Bar TextField
        OutlinedTextField(
            value = searchPhrase,
            onValueChange = onSearchChange,
            placeholder = { Text("Enter Search Phrase") },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color(0xFFF4CE14)
            ),
            shape = MaterialTheme.shapes.medium
        )
    }
}



@Composable
fun MenuBreakdown(categories: List<String>, selectedCategory: String, onCategoryClick: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(text = "ORDER FOR DELIVERY!", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            categories.forEach { category ->
                Button(
                    onClick = { onCategoryClick(category) },
                    colors = ButtonDefaults.buttonColors(
                        // Highlight the button if it's selected
                        containerColor = if (selectedCategory == category) Color(0xFF495E57) else Color(0xFFEDEFEE)
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = category.replaceFirstChar { it.uppercase() },
                        color = if (selectedCategory == category) Color.White else Color(0xFF495E57),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        HorizontalDivider(modifier = Modifier.padding(top = 16.dp), thickness = 1.dp, color = Color.LightGray)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuItemsList(items: List<MenuItemRoom>) {
    LazyColumn(modifier = Modifier.fillMaxHeight().padding(16.dp)) {
        items(items) { item ->
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            Column(modifier = Modifier.padding(vertical = 10.dp)) {
                Text(item.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.description, color = Color.Gray, modifier = Modifier.padding(vertical = 5.dp))
                        Text("$${item.price}", fontWeight = FontWeight.SemiBold)
                    }
                    GlideImage(
                        model = item.image,
                        contentDescription = "Menu Image",
                        modifier = Modifier.size(80.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}
