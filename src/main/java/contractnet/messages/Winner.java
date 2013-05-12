package contractnet.messages;

import shared.MyMessage;
import shared.MyParcel;

public class Winner extends MyMessage{

	public final MyParcel parcel;
	
	public Winner(MyParcel parcel){
		this.parcel = parcel;
	}
}
