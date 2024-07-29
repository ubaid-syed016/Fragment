package com.example.contactsapp;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class ContactListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private List<Contact> contactList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        contactList = getContacts();
        adapter = new ContactAdapter(contactList, contact -> {
            Bundle bundle = new Bundle();
            bundle.putString("name", contact.getName());
            bundle.putString("number", contact.getNumber());
            getParentFragmentManager().setFragmentResult("contact", bundle);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, ContactDetailFragment.class, bundle)
                    .addToBackStack(null)
                    .commit();
        });
        recyclerView.setAdapter(adapter);
        return view;
    }

    private List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        Cursor cursor = getContext().getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contacts.add(new Contact(name, number));
            }
            cursor.close();
        }
        return contacts;
    }
}
