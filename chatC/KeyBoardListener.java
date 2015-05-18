package chatC;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyBoardListener implements KeyListener {
	DataInterface data;

	public KeyBoardListener(DataInterface data) {
		super();
		this.data = data;
	}

	@SuppressWarnings("static-access")
	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		//		System.out.println(keyCode);
		//If the chat box if focused and the keycode is backspace
		if (keyCode == 8) {
			if (data.getChatBox().isFocused()) {
				data.getChatBox().deleteChar();
			} else if (data.getuLogin().isFocused()) {
				data.getuLogin().deleteChar();
			} else if (data.getpLogin().isFocused()) {
				data.getpLogin().deleteChar();
			}
			//10 = Enter 9 = tab
		} else if (keyCode == 10 || keyCode == KeyEvent.VK_TAB) {
			//If the chat box is focused
			if (data.getChatBox().isFocused()) {
				//Write the message
				data.getChatBox().setShouldWrite(true);
				//If the user box is focused
			} else if (data.getuLogin().isFocused()) {
				//Focus on the next box
				data.getuLogin().setFocused(false);
				data.getpLogin().setFocused(true);
			} else if (data.getpLogin().isFocused()) {
				//If its in focused on the second imput box
				//And is in the user screen
				if (data.isInLoginScreen()) {
					long time2 = data.getLoginTime();
					long time = System.currentTimeMillis();
					if (time - time2 > 15000) {
						data.setLoginTime(time);
						String user = data.getuLogin().toString();
						String pass = data.getpLogin().getContent();
						data.getSocket().connect();
						String localUserName = data.getXml().getLocalUserName();
						if (localUserName !=null) {
							data.getSocket().write("login " + user + " " + pass + "\n");
						} else {
							ClientUser localUser = data.getXml().getLocalUser();
							localUser.setUsername(user);
							localUser.generateKeys();
							data.getSocket().write("create " + user + " " + pass + " "+ localUser.getPublicKey()+ " " + localUser.getPublicMod() + '\n');
						}
					}
				} else if (data.isConnectMenu()) {
					//Or add the user 
					if (data.getpLogin().toString().indexOf(' ') == -1) {
						data.getpLogin().appendChar(' ');
					}
					//data.getXml().addUser(data.getpLogin().toString(), data.getuLogin().toString());
					data.getSocket().write("addFriend " + data.getuLogin().toString() + "\n");
					//TODO Get User Info 
					data.setConnectMenu(false);
					data.setFirstRunSinceLogin(true);
				}
			}
			data.setBigChange(true);
			//If the key pressed is not: backspace, shift, function, control, command
		} else if (keyCode !=8 && keyCode !=16 && keyCode !=0 && keyCode !=17 && keyCode !=18 && keyCode !=157) {
			//ChatBox
			if (data.getChatBox().isFocused()) {
				data.getChatBox().appendChar(e.getKeyChar());
			} else if (data.getuLogin().isFocused()) {
				data.getuLogin().appendChar(e.getKeyChar());
			} else if (data.getpLogin().isFocused()) {
				data.getpLogin().appendChar(e.getKeyChar());
			}
		}
		data.setSomethingChanged(true);
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

}
