package lukaapps.net.mylist.db;

import java.util.List;

import lukaapps.net.mylist.model.EntryList;

public interface IEntryListDAO {
    List<EntryList> getLists();
    EntryList getList(long id);
    EntryList saveList(EntryList entryList);
    boolean deleteLists(List<EntryList> lists);
    boolean deleteList(long id);
}


