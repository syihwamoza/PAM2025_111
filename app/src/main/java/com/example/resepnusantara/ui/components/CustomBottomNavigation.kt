package com.example.resepnusantara.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.resepnusantara.ui.BottomNavItem

@Composable
fun CustomBottomNavigation(
    items: List<BottomNavItem>,
    currentRoute: String?,
    onItemClick: (BottomNavItem) -> Unit
) {
    val navShape = RoundedCornerShape(topStart = 38.dp, topEnd = 38.dp)
    val deepBrown = Color(0xFF5D4037)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .background(Color(0xFFFFF8E1)) // Matches screen background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(navShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val selected = currentRoute == item.route
                    val contentColor = if (selected) deepBrown else Color.Gray.copy(alpha = 0.7f)

                    Column(
                        modifier = Modifier
                            .clickable { onItemClick(item) }
                            .padding(horizontal = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(if (selected) 44.dp else 32.dp)
                                .clip(CircleShape)
                                .background(if (selected) deepBrown.copy(alpha = 0.12f) else Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = contentColor,
                                modifier = Modifier.size(if (selected) 28.dp else 24.dp)
                            )
                        }
                        
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium,
                            color = contentColor,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
