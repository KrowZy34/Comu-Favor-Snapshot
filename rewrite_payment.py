with open('app/src/main/res/layout/activity_payment_methods.xml', 'r') as f:
    lines = f.readlines()

new_lines = []
for i, line in enumerate(lines):
    if i < 10 and '<LinearLayout xmlns:android' in line:
        line = line.replace('<LinearLayout', '<androidx.constraintlayout.widget.ConstraintLayout\n    xmlns:app="http://schemas.android.com/apk/res-auto"')
    elif i < 15 and 'android:orientation="vertical"' in line:
        line = "" # Remove orientation
    elif i > len(lines) - 5 and '</LinearLayout>' in line:
        line = line.replace('</LinearLayout>', '</androidx.constraintlayout.widget.ConstraintLayout>')
    new_lines.append(line)

content = "".join(new_lines)

# Now attach constraints to children.
# Header:
content = content.replace(
'''    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:gravity="center_vertical"
        android:paddingTop="48dp"
        android:paddingBottom="16dp"
        android:paddingStart="16dp"
        android:paddingEnd="16dp">''',
'''    <LinearLayout
        android:id="@+id/paymentHeader"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:gravity="center_vertical"
        android:paddingTop="48dp"
        android:paddingBottom="16dp"
        android:paddingStart="16dp"
        android:paddingEnd="16dp"
        app:layout_constraintTop_toTopOf="parent">''')

# ScrollView
content = content.replace(
'''    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:fillViewport="true"
        android:scrollbars="none">''',
'''    <ScrollView
        android:id="@+id/paymentScroll"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:layout_constraintTop_toBottomOf="@id/paymentHeader"
        app:layout_constraintBottom_toTopOf="@id/paymentBottomButtons"
        app:layout_constraintStart_toStartOf="parent"
        android:fillViewport="true"
        android:scrollbars="none">''')

# Bottom section:
content = content.replace(
'''    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:paddingStart="24dp"
        android:paddingEnd="24dp"
        android:paddingBottom="32dp"
        android:paddingTop="16dp"
        android:background="@drawable/bg_bottom_gradient">''',
'''    <LinearLayout
        android:id="@+id/paymentBottomButtons"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:paddingStart="24dp"
        android:paddingEnd="24dp"
        android:paddingBottom="32dp"
        android:paddingTop="16dp"
        android:background="@drawable/bg_bottom_gradient"
        app:layout_constraintBottom_toBottomOf="parent">''')

with open('app/src/main/res/layout/activity_payment_methods.xml', 'w') as f:
    f.write(content)
