/*
This is the NullAccount class which extends the Account class and is used to implement the
null design pattern which handles situations for when a client does not have an open account of
any specific type yet.
 */

public class NullAccount extends Account{

    public NullAccount() {
        super(0);
        super.setOpen(false);
    }

    @Override
    public void closeAccount() {
        // cant close account that doesnt exist, do nothing
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public boolean payFee(double fee) {
        return false;
    }
}
