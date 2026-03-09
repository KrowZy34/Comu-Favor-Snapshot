import re

with open('app/src/main/res/layout/activity_rec_post.xml', 'r') as f:
    content = f.read()

# Replace root LinearLayout with ConstraintLayout
content = re.sub(
    r'<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"\s+xmlns:app="http://schemas.android.com/apk/res-auto"\s+xmlns:tools="http://schemas.android.com/tools"\s+android:id="@+id/recPostMain"\s+android:layout_width="match_parent"\s+android:layout_height="match_parent"\s+android:orientation="vertical"\s+android:background="@color/black"\s+tools:context=".RecPostActivity">',
    '<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"\n    xmlns:app="http://schemas.android.com/apk/res-auto"\n    xmlns:tools="http://schemas.android.com/tools"\n    android:id="@+id/recPostMain"\n    android:layout_width="match_parent"\n    android:layout_height="match_parent"\n    android:background="@color/black"\n    tools:context=".RecPostActivity">',
    content
)

# Close root
lines = content.split('\n')
for i in range(len(lines)-1, -1, -1):
    if '</LinearLayout>' in lines[i]:
        lines[i] = lines[i].replace('</LinearLayout>', '</androidx.constraintlayout.widget.ConstraintLayout>')
        break
content = '\n'.join(lines)

# Set constraints
content = content.replace('<LinearLayout\n        android:layout_width="match_parent"\n        android:layout_height="wrap_content"\n        android:orientation="horizontal"\n        android:gravity="center_vertical"\n        android:paddingTop="48dp"\n        android:paddingBottom="16dp"\n        android:paddingStart="16dp"\n        android:paddingEnd="16dp">', '<LinearLayout\n        android:id="@+id/recPostHeader"\n        android:layout_width="match_parent"\n        android:layout_height="wrap_content"\n        android:orientation="horizontal"\n        android:gravity="center_vertical"\n        android:paddingTop="48dp"\n        android:paddingBottom="16dp"\n        android:paddingStart="16dp"\n        android:paddingEnd="16dp"\n        app:layout_constraintTop_toTopOf="parent">')

content = content.replace('<ScrollView\n        android:layout_width="match_parent"\n        android:layout_height="0dp"\n        android:layout_weight="1"', '<ScrollView\n        android:id="@+id/recPostScroll"\n        android:layout_width="match_parent"\n        android:layout_height="0dp"\n        app:layout_constraintTop_toBottomOf="@id/recPostHeader"\n        app:layout_constraintBottom_toTopOf="@id/bottomNavDivider"\n        app:layout_constraintStart_toStartOf="parent"')

content = content.replace('<View\n        android:layout_width="match_parent"\n        android:layout_height="1dp"\n        android:background="@color/card_border" />', '<View\n        android:id="@+id/bottomNavDivider"\n        android:layout_width="match_parent"\n        android:layout_height="1dp"\n        android:background="@color/card_border"\n        app:layout_constraintBottom_toTopOf="@id/bottomNavMain" />')

content = content.replace('<LinearLayout\n        android:layout_width="match_parent"\n        android:layout_height="wrap_content"\n        android:orientation="horizontal"\n        android:background="@color/black"\n        android:paddingTop="8dp"\n        android:paddingBottom="8dp"\n        android:weightSum="5">', '<LinearLayout\n        android:id="@+id/bottomNavMain"\n        android:layout_width="match_parent"\n        android:layout_height="wrap_content"\n        android:orientation="horizontal"\n        android:background="@color/black"\n        android:paddingTop="8dp"\n        android:paddingBottom="8dp"\n        android:weightSum="5"\n        app:layout_constraintBottom_toBottomOf="parent">')


with open('app/src/main/res/layout/activity_rec_post.xml', 'w') as f:
    f.write(content)

