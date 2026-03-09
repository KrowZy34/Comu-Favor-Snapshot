import re

with open('app/src/main/res/layout/activity_postulados.xml', 'r') as f:
    content = f.read()

# Replace root LinearLayout with ConstraintLayout
content = re.sub(
    r'<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"\s+xmlns:app="http://schemas.android.com/apk/res-auto"\s+xmlns:tools="http://schemas.android.com/tools"\s+android:id="@+id/postuladosMain"\s+android:layout_width="match_parent"\s+android:layout_height="match_parent"\s+android:orientation="vertical"\s+android:background="@color/black"\s+tools:context=".PostuladosActivity">',
    '<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"\n    xmlns:app="http://schemas.android.com/apk/res-auto"\n    xmlns:tools="http://schemas.android.com/tools"\n    android:id="@+id/postuladosMain"\n    android:layout_width="match_parent"\n    android:layout_height="match_parent"\n    android:background="@color/black"\n    tools:context=".PostuladosActivity">',
    content
)
# Close root
content = content.replace('</LinearLayout>\n\n</LinearLayout>', '</LinearLayout>\n\n</androidx.constraintlayout.widget.ConstraintLayout>')
content = re.sub(r'</LinearLayout>\s*$', '</androidx.constraintlayout.widget.ConstraintLayout>\n', content)

# Add IDs to un-ID'd elements at root level for constraint linking
content = content.replace('<Space\n        android:layout_width="match_parent"\n        android:layout_height="48dp" />', '<Space\n        android:id="@+id/topSpace"\n        android:layout_width="match_parent"\n        android:layout_height="48dp"\n        app:layout_constraintTop_toTopOf="parent" />')

content = content.replace('<ScrollView\n        android:layout_width="match_parent"\n        android:layout_height="0dp"\n        android:layout_weight="1"', '<ScrollView\n        android:id="@+id/mainScrollView"\n        android:layout_width="match_parent"\n        android:layout_height="0dp"\n        app:layout_constraintTop_toBottomOf="@id/topSpace"\n        app:layout_constraintBottom_toTopOf="@id/llAsignadoView"\n        app:layout_constraintStart_toStartOf="parent"\n        app:layout_constraintEnd_toEndOf="parent"')

content = content.replace('android:id="@+id/llAsignadoView"\n        android:layout_width="match_parent"\n        android:layout_height="wrap_content"\n        android:orientation="vertical"', 'android:id="@+id/llAsignadoView"\n        android:layout_width="match_parent"\n        android:layout_height="wrap_content"\n        android:orientation="vertical"\n        app:layout_constraintBottom_toTopOf="@id/bottomNavDivider"\n        app:layout_constraintStart_toStartOf="parent"\n        app:layout_constraintEnd_toEndOf="parent"')

content = content.replace('<View\n        android:layout_width="match_parent"\n        android:layout_height="1dp"\n        android:background="@color/card_border" />', '<View\n        android:id="@+id/bottomNavDivider"\n        android:layout_width="match_parent"\n        android:layout_height="1dp"\n        android:background="@color/card_border"\n        app:layout_constraintBottom_toTopOf="@id/bottomNavMain" />')

content = content.replace('<LinearLayout\n        android:layout_width="match_parent"\n        android:layout_height="wrap_content"\n        android:orientation="horizontal"\n        android:background="@color/black"\n        android:paddingTop="8dp"\n        android:paddingBottom="8dp"\n        android:weightSum="5">', '<LinearLayout\n        android:id="@+id/bottomNavMain"\n        android:layout_width="match_parent"\n        android:layout_height="wrap_content"\n        android:orientation="horizontal"\n        android:background="@color/black"\n        android:paddingTop="8dp"\n        android:paddingBottom="8dp"\n        android:weightSum="5"\n        app:layout_constraintBottom_toBottomOf="parent">')

with open('app/src/main/res/layout/activity_postulados.xml', 'w') as f:
    f.write(content)

