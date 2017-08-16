package edu.rosehulman.yangh4.roommate;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Stack;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GroupMemberAdapterFragment.Callback, GroupListFragment.Callback, Welcome_Fragment.Callback, ItemListFragment.Callback, AddItemFragment.Callback, CreateGroupFragment.Callback, GoogleApiClient.OnConnectionFailedListener {

    public static People user;
    private Stack<Fragment> mFragmentStack;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mToggle;
    public static NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private boolean showoverflowbutton;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    OnCompleteListener mOnCompleteListener;
    private GoogleApiClient mGoogleApiClient;
    private DatabaseReference mMessageReference;
    private int RC_SIGN_IN = 1;
    public static Activity activity;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        showoverflowbutton = false;
        mFragmentStack = new Stack<>();
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
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        initializeListeners();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("TOKENGET", "Refreshed token: " + refreshedToken);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
        //Credit to https://stackoverflow.com/questions/7469082/getting-exception-illegalstateexception-can-not-perform-this-action-after-onsa/10261438#10261438
    }

    private void initializeListeners() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    setDrawerState(true);
                    final DatabaseReference mMemberRef = FirebaseDatabase.getInstance().getReference().child("members/" + user.getUid());
                    final String uid = user.getUid();
                    mMemberRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            People peopleToAdd = dataSnapshot.getValue(People.class);
                            if (peopleToAdd != null) {
                                Log.d("Datachange", "not new user");
                                MainActivity.user = peopleToAdd;
                                switchToGroupListFragment(uid);
                                mMessageReference = FirebaseDatabase.getInstance().getReference().child("messages");
                                mMessageReference.addChildEventListener(new MessageListener());
                                ((TextView) MainActivity.mNavigationView.findViewById(R.id.user_name_text)).setText(MainActivity.user.getName());
                                ((TextView) MainActivity.mNavigationView.findViewById(R.id.user_email_text)).setText(MainActivity.user.getEmail());
                                return;
                            }

                            Log.d("Datachange", "new user");
                            MainActivity.user = new People("First Name", "Last Name", "Email", "Phone");
                            MainActivity.user.setKey(user.getUid());
                            mMemberRef.setValue(MainActivity.user);
                            switchToGroupListFragment(uid);
                            mFragmentStack.push(new EditProfileFragment());
                            replacefragment(mFragmentStack.peek());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    switchToLoginFragment();
                }
            }
        };
        mOnCompleteListener = new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (!task.isSuccessful()) {
                    showLoginError("Login Failed");
                } else {
                    Log.d("LOGINFAIL", "Login success");
                }
            }
        };
    }

    private void switchToGroupListFragment(String uid) {
        mFragmentStack.clear();
        mFragmentStack.push(GroupListFragment.newInstance(uid));
        replacefragment(mFragmentStack.peek());
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

    private void showLoginError(String message) {
        Welcome_Fragment welcomeFragment = (Welcome_Fragment) getSupportFragmentManager().findFragmentByTag("Login");
        welcomeFragment.onLoginError(message);
    }

    private void switchToLoginFragment() {
        mFragmentStack.push(new Welcome_Fragment());
        setDrawerState(false);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, mFragmentStack.peek(), "Login");
        ft.commit();
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
            case R.id.action_edit_profile:
                mFragmentStack.push(new EditProfileFragment());
                replacefragment(mFragmentStack.peek());
                break;
            case R.id.action_logout:
                mFragmentStack.clear();
                mAuth.signOut();
                user = null;
                switchToLoginFragment();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void show_leave_group_dialog(final GroupMembersAdapter mGroupMembersAdapter) {
        final Context context = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("WARNING!");
        builder.setMessage("Are you sure you want to leave the group?");
        builder.setNegativeButton("CANCEL", null);
        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final DatabaseReference mGroupRef = FirebaseDatabase.getInstance().getReference().child("groups/" + mGroupMembersAdapter.getId());
                mGroupRef.child("balanceMap/" + user.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null && Double.parseDouble(dataSnapshot.getValue().toString()) != (0)) {
                            Log.d("CURRENTBALANCE", "BALANCE:" + dataSnapshot.getValue().toString());
                            new AlertDialog.Builder(context)
                                    .setTitle("WARNING")
                                    .setMessage("You can't leave before you pay your balance")
                                    .setPositiveButton("OK", null)
                                    .create()
                                    .show();
                        } else {
                            mGroupRef.child("memberkeys/" + user.getKey()).removeValue();
                            mGroupRef.child("balanceMap/" + user.getKey()).removeValue();
                            FirebaseDatabase.getInstance().getReference().child("members/" + user.getKey() + "/groupkeys/" + mGroupMembersAdapter.getId()).removeValue();
                            mFragmentStack.pop();//Remove current fragment from stack since we left the group
                            replacefragment(mFragmentStack.peek());//Return to the last fragment. Note that if we can quit the group, the last fragment must be the group list fragment
                            ((GroupListFragment) mFragmentStack.peek()).getmGroupAdapter().notifyDataSetChanged();
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        builder.create().show();
    }

    @Override
    public void show_item(final String groupKey) {
        mFragmentStack.push(ItemListFragment.newInstance(groupKey));
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

    @Override
    public void joinGroup(GroupAdapter mGroupAdapter) {
        mFragmentStack.push(JoinGroupFragment.newInstance(mGroupAdapter));
        replacefragment(mFragmentStack.peek());
    }

    @Override
    public void showEditGroupDialog(final Group group) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit " + group.getId());
        View view = getLayoutInflater().inflate(R.layout.dialog_edit, null, false);
        builder.setView(view);
        final EditText GroupName = (EditText) view.findViewById(R.id.dialog_edit_group_name);
        final EditText GroupDescription = (EditText) view.findViewById(R.id.dialog_group_description);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = GroupName.getText().toString();
                String desc = GroupDescription.getText().toString();
                String groupid = group.getId();
                group.setDescription(desc);
                group.setGroupname(name);
                DatabaseReference mGroupRef = FirebaseDatabase.getInstance().getReference().child("groups/" + groupid);
                mGroupRef.setValue(group);
            }
        }).show();
    }


    public void replacefragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    @Override
    public void showItemDetail(Item item) {
        mFragmentStack.push(ItemDetailFragment.newInstance(item));
        replacefragment(mFragmentStack.peek());
    }

    @Override
    public void addItem(ItemListAdapter mAdapter) {
        mFragmentStack.push(AddItemFragment.newInstance(mAdapter, true));
        replacefragment(mFragmentStack.peek());
    }

    @Override
    public void showEditDialog(ItemListAdapter mAdapter, Item item) {
        mFragmentStack.push(AddItemFragment.newInstance(mAdapter, item, false));
        replacefragment(mFragmentStack.peek());
    }

    @Override
    public void backtolastlevel() {
        mFragmentStack.pop();
        replacefragment(mFragmentStack.peek());
    }

    @Override
    public void onLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(mOnCompleteListener);
    }

    @Override
    public void onGoogleLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void showInviteDialog(String message, final String groupid) {
        final Context context = this;
        Log.d("HEYHEYHEY", "YOUGOTANEWMESSAGE");
        new AlertDialog.Builder(this)
                .setTitle("You got a new invitation")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final DatabaseReference mGroupRef = FirebaseDatabase.getInstance().getReference().child("groups");
                        DatabaseReference theGroupRef = mGroupRef.child(groupid);
                        theGroupRef.child("memberkeys/" + MainActivity.user.getKey()).setValue(true);
                        theGroupRef.child("balanceMap/" + MainActivity.user.getKey()).setValue(0);
                        FirebaseDatabase.getInstance().getReference().child("members/" + MainActivity.user.getKey() + "/groupkeys/" + groupid).setValue(true);
                        Toast.makeText(context, "You successfully joined the group", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Nope", null)
                .create()
                .show();
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private class MessageListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.d("HEYHEYHEY", "YOUGOTANEWMESSAGE");
            if (dataSnapshot.child("receiverid").getValue().equals(MainActivity.user.getKey())) {
                showInviteDialog(dataSnapshot.child("message").getValue().toString(), dataSnapshot.child("groupid").getValue().toString());
                dataSnapshot.getRef().removeValue();
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}