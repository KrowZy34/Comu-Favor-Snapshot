import re

with open('app/src/main/res/layout/activity_create_account.xml', 'r') as f:
    content = f.read()

# Replace root FrameLayout with ConstraintLayout
content = re.sub(
    r'<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"\s+xmlns:tools="http://schemas.android.com/tools"\s+android:id="@+id/createAccountMain"\s+android:layout_width="match_parent"\s+android:layout_height="match_parent"\s+tools:context=".CreateAccountActivity">',
    '<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"\n    xmlns:app="http://schemas.android.com/apk/res-auto"\n    xmlns:tools="http://schemas.android.com/tools"\n    android:id="@+id/createAccountMain"\n    android:layout_width="match_parent"\n    android:layout_height="match_parent"\n    tools:context=".CreateAccountActivity">',
    content
)

# Close root
content = content.replace('</FrameLayout>', '</androidx.constraintlayout.widget.ConstraintLayout>')

constraints = '\n        app:layout_constraintTop_toTopOf="parent"\n        app:layout_constraintBottom_toBottomOf="parent"\n        app:layout_constraintStart_toStartOf="parent"\n        app:layout_constraintEnd_toEndOf="parent"'

content = content.replace('<ImageView\n        android:layout_width="match_parent"\n        android:layout_height="match_parent"\n        android:scaleType="centerCrop"\n        android:src="@drawable/bg_login"\n        android:contentDescription="@string/app_name" />', '<ImageView\n        android:layout_width="match_parent"\n        android:layout_height="match_parent"\n        android:scaleType="centerCrop"\n        android:src="@drawable/bg_login"\n        android:contentDescription="@string/app_name"' + constraints + ' />')

content = content.replace('<View\n        android:layout_width="match_parent"\n        android:layout_height="match_parent"\n        android:background="@color/dark_overlay" />', '<View\n        android:layout_width="match_parent"\n        android:layout_height="match_parent"\n        android:background="@color/dark_overlay"' + constraints + ' />')

content = content.replace('<ScrollView\n        android:layout_width="match_parent"\n        android:layout_height="match_parent"\n        android:fillViewport="true"\n        android:scrollbars="none">', '<ScrollView\n        android:layout_width="match_parent"\n        android:layout_height="match_parent"\n        android:fillViewport="true"\n        android:scrollbars="none"' + constraints + '>')

with open('app/src/main/res/layout/activity_create_account.xml', 'w') as f:
    f.write(content)

