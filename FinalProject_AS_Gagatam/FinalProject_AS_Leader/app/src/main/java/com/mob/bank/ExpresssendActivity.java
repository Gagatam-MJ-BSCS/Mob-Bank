package com.mob.bank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.*;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.util.*;
import java.util.regex.*;
import java.text.*;
import org.json.*;
import java.util.HashMap;
import java.util.ArrayList;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.content.Intent;
import android.net.Uri;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.view.View;
import java.text.DecimalFormat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.DialogFragment;


public class ExpresssendActivity extends  AppCompatActivity  { 
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private String uid = "";
	private double accountLength = 0;
	private boolean accounExsistBoolean = false;
	private double index = 0;
	private String receiverkey = "";
	private String mobilenumber = "";
	private String receiversname = "";
	private String ammount = "";
	private String newAmmount = "";
	private String yourbalance = "";
	private HashMap<String, Object> createNotificationMap = new HashMap<>();
	private String sendername = "";
	private HashMap<String, Object> createLogsMap = new HashMap<>();
	private String receiversnumber = "";
	
	private ArrayList<HashMap<String, Object>> accountslm = new ArrayList<>();
	private ArrayList<String> accountsListString = new ArrayList<>();
	
	private LinearLayout main;
	private LinearLayout head;
	private LinearLayout body;
	private ImageView btnBack;
	private TextView textview1;
	private LinearLayout linear2;
	private EditText tbMobileNumber;
	private TextView textview3;
	private EditText tbAmmount;
	private Button btnAccept;
	private TextView textview2;
	private TextView textview4;
	
	private DatabaseReference fbdb = _firebase.getReference("accounts");
	private ChildEventListener _fbdb_child_listener;
	private FirebaseAuth fbauth;
	private OnCompleteListener<Void> fbauth_updateEmailListener;
	private OnCompleteListener<Void> fbauth_updatePasswordListener;
	private OnCompleteListener<Void> fbauth_emailVerificationSentListener;
	private OnCompleteListener<Void> fbauth_deleteUserListener;
	private OnCompleteListener<Void> fbauth_updateProfileListener;
	private OnCompleteListener<AuthResult> fbauth_phoneAuthListener;
	private OnCompleteListener<AuthResult> fbauth_googleSignInListener;
	private OnCompleteListener<AuthResult> _fbauth_create_user_listener;
	private OnCompleteListener<AuthResult> _fbauth_sign_in_listener;
	private OnCompleteListener<Void> _fbauth_reset_password_listener;
	private Intent i = new Intent();
	private AlertDialog.Builder d;
	private DatabaseReference fbdbnotification = _firebase.getReference("notification");
	private ChildEventListener _fbdbnotification_child_listener;
	private DatabaseReference fbdbLogs = _firebase.getReference("logs");
	private ChildEventListener _fbdbLogs_child_listener;
	private Calendar c = Calendar.getInstance();
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.expresssend);
		initialize(_savedInstanceState);
		com.google.firebase.FirebaseApp.initializeApp(this);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		main = (LinearLayout) findViewById(R.id.main);
		head = (LinearLayout) findViewById(R.id.head);
		body = (LinearLayout) findViewById(R.id.body);
		btnBack = (ImageView) findViewById(R.id.btnBack);
		textview1 = (TextView) findViewById(R.id.textview1);
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		tbMobileNumber = (EditText) findViewById(R.id.tbMobileNumber);
		textview3 = (TextView) findViewById(R.id.textview3);
		tbAmmount = (EditText) findViewById(R.id.tbAmmount);
		btnAccept = (Button) findViewById(R.id.btnAccept);
		textview2 = (TextView) findViewById(R.id.textview2);
		textview4 = (TextView) findViewById(R.id.textview4);
		fbauth = FirebaseAuth.getInstance();
		d = new AlertDialog.Builder(this);
		
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				finish();
			}
		});
		
		btnAccept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (tbMobileNumber.getText().toString().equals("") || !(tbMobileNumber.getText().toString().length() == 11)) {
					((EditText)tbMobileNumber).setError("The cellphone number is required and must be contain 11 numbers.");
				}
				else {
					if (tbAmmount.getText().toString().equals("") || (Double.parseDouble(tbAmmount.getText().toString()) < 1)) {
						((EditText)tbAmmount).setError("The amount is required and must be greater than 0.");
					}
					else {
						if (tbMobileNumber.getText().toString().equals(mobilenumber)) {
							((EditText)tbMobileNumber).setError("You cant send money to your own account.");
						}
						else {
							fbdb.addListenerForSingleValueEvent(new ValueEventListener() {
									@Override
									public void onDataChange(DataSnapshot _dataSnapshot) {
											accountsListString = new ArrayList<>();
									        GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
									        for (DataSnapshot _data : _dataSnapshot.getChildren()) {
										            accountsListString.add(_data.getKey());
										        }
									        index = 0;
									accounExsistBoolean = false;
									for(int _repeat35 = 0; _repeat35 < (int)(accountsListString.size()); _repeat35++) {
										if (!accountsListString.get((int)(index)).equals(uid) && _dataSnapshot.child(accountsListString.get((int)(index))).child("mobilenumber").getValue().toString().equals(tbMobileNumber.getText().toString())) {
											receiverkey = accountsListString.get((int)(index));
											accounExsistBoolean = true;
										}
										index++;
									}
									if (accounExsistBoolean) {
										sendername = _dataSnapshot.child(uid).child("firstname").getValue().toString().concat(" ".concat(_dataSnapshot.child(uid).child("lastname").getValue().toString()));
										receiversname = _dataSnapshot.child(receiverkey).child("firstname").getValue().toString().concat(" ".concat(_dataSnapshot.child(receiverkey).child("lastname").getValue().toString()));
										receiversnumber = _dataSnapshot.child(receiverkey).child("mobilenumber").getValue().toString();
										ammount = tbAmmount.getText().toString();
										newAmmount = String.valueOf(Double.parseDouble(_dataSnapshot.child(receiverkey).child("balance").getValue().toString()) + Double.parseDouble(ammount));
										yourbalance = _dataSnapshot.child(uid).child("balance").getValue().toString();
										d.setTitle("EXPRESS SEND");
										d.setMessage("You want to send money " + receiversname + " with the ammount of ₱" + ammount + "?");
										d.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface _dialog, int _which) {
												if (Double.parseDouble(ammount) <= Double.parseDouble(yourbalance)) {
													fbdb.child(uid).child("balance").setValue(String.valueOf(Double.parseDouble(yourbalance) - Double.parseDouble(ammount)));
													fbdb.child(receiverkey).child("balance").setValue(newAmmount);
													_CreateLogs(uid, "You've sent money ₱" + ammount + " to " + mobilenumber + ".");
													_CreateNotification(sendername, receiverkey, "NEW BALANCE", "You receive ₱" + ammount + " from " + mobilenumber + ".");
													tbMobileNumber.setText("");
													tbAmmount.setText("0");
													SketchwareUtil.showMessage(getApplicationContext(), "Sent successfully");
												}
												else {
													SketchwareUtil.showMessage(getApplicationContext(), "Your balance is not enough to send money.");
												}
											}
										});
										d.setNegativeButton("No", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface _dialog, int _which) {
												
											}
										});
										d.create().show();
									}
									else {
										((EditText)tbMobileNumber).setError("This receiver number is not exist.");
									}
									}
									@Override
									public void onCancelled(DatabaseError _databaseError) {
									}
							});
						}
					}
				}
			}
		});
		
		_fbdb_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onChildMoved(DataSnapshot _param1, String _param2) {
				
			}
			
			@Override
			public void onChildRemoved(DataSnapshot _param1) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onCancelled(DatabaseError _param1) {
				final int _errorCode = _param1.getCode();
				final String _errorMessage = _param1.getMessage();
				
			}
		};
		fbdb.addChildEventListener(_fbdb_child_listener);
		
		_fbdbnotification_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onChildMoved(DataSnapshot _param1, String _param2) {
				
			}
			
			@Override
			public void onChildRemoved(DataSnapshot _param1) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onCancelled(DatabaseError _param1) {
				final int _errorCode = _param1.getCode();
				final String _errorMessage = _param1.getMessage();
				
			}
		};
		fbdbnotification.addChildEventListener(_fbdbnotification_child_listener);
		
		_fbdbLogs_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onChildMoved(DataSnapshot _param1, String _param2) {
				
			}
			
			@Override
			public void onChildRemoved(DataSnapshot _param1) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onCancelled(DatabaseError _param1) {
				final int _errorCode = _param1.getCode();
				final String _errorMessage = _param1.getMessage();
				
			}
		};
		fbdbLogs.addChildEventListener(_fbdbLogs_child_listener);
		
		fbauth_updateEmailListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		fbauth_updatePasswordListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		fbauth_emailVerificationSentListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		fbauth_deleteUserListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		fbauth_phoneAuthListener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> task){
				final boolean _success = task.isSuccessful();
				final String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";
				
			}
		};
		
		fbauth_updateProfileListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		fbauth_googleSignInListener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> task){
				final boolean _success = task.isSuccessful();
				final String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";
				
			}
		};
		
		_fbauth_create_user_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		_fbauth_sign_in_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		_fbauth_reset_password_listener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				
			}
		};
	}
	
	private void initializeLogic() {
		_design();
		if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
			uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
			fbdb.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot _dataSnapshot) {
					accountslm = new ArrayList<>();
					try {
						GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
						for (DataSnapshot _data : _dataSnapshot.getChildren()) {
							HashMap<String, Object> _map = _data.getValue(_ind);
							accountslm.add(_map);
						}
					}
					catch (Exception _e) {
						_e.printStackTrace();
					}
					mobilenumber = _dataSnapshot.child(uid).child("mobilenumber").getValue().toString();
				}
				@Override
				public void onCancelled(DatabaseError _databaseError) {
				}
			});
		}
		else {
			i.setClass(getApplicationContext(), MainActivity.class);
			startActivity(i);
			finish();
		}
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	public void _design () {
		tbMobileNumber.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)5, (int)1, 0xFF000000, 0xFFFFFFFF));
		tbAmmount.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)5, (int)1, 0xFF000000, 0xFFFFFFFF));
	}
	
	
	public void _CreateNotification (final String _name, final String _to, final String _title, final String _message) {
		c = Calendar.getInstance();
		createNotificationMap = new HashMap<>();
		createNotificationMap.put("notification_key", fbdbnotification.push().getKey());
		createNotificationMap.put("name", _name);
		createNotificationMap.put("to", _to);
		createNotificationMap.put("title", _title);
		createNotificationMap.put("message", _message);
		createNotificationMap.put("when", new SimpleDateFormat("yyy-MM-dd hh:mm a").format(c.getTime()));
		fbdbnotification.push().updateChildren(createNotificationMap);
	}
	
	
	public void _CreateLogs (final String _userid, final String _message) {
		c = Calendar.getInstance();
		createLogsMap = new HashMap<>();
		createLogsMap.put("log_id", fbdbLogs.push().getKey());
		createLogsMap.put("userid", _userid);
		createLogsMap.put("message", _message);
		createLogsMap.put("when", new SimpleDateFormat("yyy-MM-dd hh:mm a").format(c.getTime()));
		fbdbLogs.push().updateChildren(createLogsMap);
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input){
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels(){
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels(){
		return getResources().getDisplayMetrics().heightPixels;
	}
	
}