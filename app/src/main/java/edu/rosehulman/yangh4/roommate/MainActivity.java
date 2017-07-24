package edu.rosehulman.yangh4.roommate;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GroupMemberAdapterFragment.Callback, GroupListFragment.Callback, Welcome_Fragment.Callback, Signup_fragment.Callback, Login_Fragment.Callback, ItemListFragment.Callback, AddItemFragment.Callback, CreateGroupFragment.Callback {

    public static User user;
    private Stack<Fragment> mFragmentStack;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private boolean showoverflowbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showoverflowbutton = false;
        mFragmentStack = new Stack<>();
        mFragmentStack.push(new Welcome_Fragment());
        replacefragment(mFragmentStack.peek());
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        setDrawerState(false);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mToggle.syncState();
    }

    //Credit to https://stackoverflow.com/questions/19439320/disabling-navigation-drawer-toggling-home-button-up-indicator-in-fragments
    public void setDrawerState(boolean isEnabled) {
        if (isEnabled) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
            mToggle.setDrawerIndicatorEnabled(true);
            mToggle.syncState();

        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mToggle.setDrawerIndicatorEnabled(false);
            mToggle.syncState();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mFragmentStack.size() > 1) {
            mFragmentStack.pop();
            replacefragment(mFragmentStack.peek());
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Credit to https://stackoverflow.com/questions/9206530/how-to-disable-hide-three-dot-indicatoroption-menu-indicator-on-ics-handsets
        if (!showoverflowbutton) {
            return true;
        }
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
//            case R.id.action_view_message:
//                break;
//            case R.id.action_bank_cards:
//                break;
            case R.id.action_edit_profile:
                mFragmentStack.push(new EditProfileFragment());
                replacefragment(mFragmentStack.peek());
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void show_leave_group_dialog(final GroupMembersAdapter mGroupMembersAdapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("WARNING!");
        builder.setMessage("Are you sure you want to leave the group?");
        builder.setNegativeButton("CANCEL", null);
        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mGroupMembersAdapter.getmGroup().getGroupmembers().remove(user);
                user.getBelonggroup().remove(mGroupMembersAdapter.getmGroup());
                mFragmentStack.pop();//Remove current fragment from stack since we left the group
                replacefragment(mFragmentStack.peek());//Return to the last fragment. Note that if we can quit the group, the last fragment must be the group list fragment
                ((GroupListFragment) mFragmentStack.peek()).getmGroupAdapter().notifyDataSetChanged();
            }
        });
        builder.create().show();
    }

    @Override
    public void show_item(final Group group) {
        mFragmentStack.push(ItemListFragment.newInstance(group));
        replacefragment(mFragmentStack.peek());
    }

    @Override
    public void showUserInfo(People person) {
        mFragmentStack.push(PeopleInfo_Fragment.newInstance(person));
        replacefragment(mFragmentStack.peek());
    }

    @Override
    public void showgroupmember(Group group) {
        mFragmentStack.push(GroupMemberAdapterFragment.newInstance(group));
        replacefragment(mFragmentStack.peek());
    }

    @Override
    public void createNewGroup(GroupAdapter mGroupAdapter) {
        mFragmentStack.push(CreateGroupFragment.newInstance(mGroupAdapter));
        replacefragment(mFragmentStack.peek());
    }


    public void replacefragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    @Override
    public void welcomelogin() {
        mFragmentStack.push(new Login_Fragment());
        replacefragment(mFragmentStack.peek());
    }

    @Override
    public void welcomesignup() {
        mFragmentStack.push(new Signup_fragment());
        replacefragment(mFragmentStack.peek());
    }

    @Override
    public void Signup(CharSequence FirstName, CharSequence LastName, CharSequence Email, CharSequence Password) {
        setUser(FirstName, LastName, Email, Password);
        mFragmentStack.pop();
        mFragmentStack.push(new Login_Fragment());
        replacefragment(mFragmentStack.peek());
    }

    public void setUser(CharSequence FirstName, CharSequence LastName, CharSequence Email, CharSequence Password) {
        user = new User();
        user.setPassword(Password.toString());
        user.setEmail(Email.toString());
        user.setLast_name(LastName.toString());
        user.setFirst_name(FirstName.toString());
        //For testing
        ArrayList<Group> test = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Group temp = new Group();
            temp.setGroupname("TEST GROUP" + i);
            temp.setBalance(i);
            temp.setDescription("DESCRIPTION for" + temp.getGroupname());
            for (int j = 0; j < 5; j++) {
                temp.addItem(new Item("ITEM" + (5 - j), "" + j, "TAG" + j, "DESCRIPTION" + j));
                temp.addMember(new People("firstname" + j, "lastname" + j, null, "sample@email.com", "123-456-7890"));
            }
            temp.updateBalance();
            test.add(temp);
        }
        //End
        user.setBelonggroup(test);
    }

    @Override
    public void Login(CharSequence email, CharSequence password) {
        //Get FirstName and LastName from firebase
        String FirstName = "Ocean";
        String LastName = "Side";
        setUser(FirstName, LastName, email, password);
        //Clear the stack since we won't go back
        mFragmentStack.clear();
        mFragmentStack.push(GroupListFragment.newInstance(user.getBelonggroup()));
        replacefragment(mFragmentStack.peek());
        //Show toolbar and drawer and overflow button
        showoverflowbutton = true;
        setDrawerState(true);
        //Probably will make a subclass or method for updating user name&email as we proceed
        //We dont have image right now
        ((TextView) mNavigationView.getHeaderView(0).findViewById(R.id.user_email_text)).setText(user.getEmail());
        ((TextView) mNavigationView.getHeaderView(0).findViewById(R.id.user_name_text)).setText(user.getName());
//        ((ImageView) navigationView.findViewById(R.id.user_photo_image)).setImageBitmap(user.getPhoto());
        hideKeyboard();
    }

    //Credit to https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void findpassword() {
        hideKeyboard();
        mFragmentStack.push(new FindPassword_Fragment());
        replacefragment(mFragmentStack.peek());
    }

    @Override
    public void showItemDetail(Item item) {
        mFragmentStack.push(ItemDetailFragment.newInstance(item));
        replacefragment(mFragmentStack.peek());
    }

    @Override
    public void addItem(ItemListAdapter mAdapter) {
        mFragmentStack.push(AddItemFragment.newInstance(mAdapter));
        replacefragment(mFragmentStack.peek());
    }

    @Override
    public void backtolastlevel() {
        hideKeyboard();
        mFragmentStack.pop();
        replacefragment(mFragmentStack.peek());
    }
}