package com.example.py7.crudkotlin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private var listPengunjung = ArrayList<Pengunjung>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            var intent = Intent(this, PengunjungActivity::class.java)
            startActivity(intent)
        }

        loadData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        var dbAdapter = DBAdapter(this)
        var cursor = dbAdapter.allQuery()

        listPengunjung.clear()
        if (cursor.moveToFirst()){
            do {
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val nama = cursor.getString(cursor.getColumnIndex("Nama"))
                val jenis = cursor.getString(cursor.getColumnIndex("Jenis"))
                val alamat = cursor.getString(cursor.getColumnIndex("Alamat"))

                listPengunjung.add(Pengunjung(id, nama, jenis, alamat))
            }while (cursor.moveToNext())
        }

        var pengunjungAdapter = PengunjungAdapter(this, listPengunjung)
        lvPengunjung.adapter = pengunjungAdapter
    }

    inner class PengunjungAdapter: BaseAdapter{

        private var pengunjungList= ArrayList<Pengunjung>()
        private var context: Context? = null

        constructor(context: Context, pengunjungList: ArrayList<Pengunjung>) : super(){
            this.pengunjungList = pengunjungList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view: View?
            val vh: ViewHolder

            if (convertView == null){
                view = layoutInflater.inflate(R.layout.pengunjung, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                Log.i("db", "set tag for ViewHolder, position: " + position)
            }else{
                view = convertView
                vh = view.tag as ViewHolder
            }

            var mPengunjung = pengunjungList[position]

            vh.tvNama.text = mPengunjung.name
            vh.tvJenis.text = mPengunjung.jenis
            vh.tvalamat.text = mPengunjung.alamat

            lvPengunjung.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
                updateBarang(mPengunjung)
            }

            return view
        }

        override fun getItem(position: Int): Any {
            return pengunjungList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return pengunjungList.size
        }

    }

    private fun updateBarang(pengunjung: Pengunjung) {
        var  intent = Intent(this, PengunjungActivity::class.java)
        intent.putExtra("MainActId", pengunjung.id)
        intent.putExtra("MainActNama", pengunjung.name)
        intent.putExtra("MainActJenis", pengunjung.jenis)
        intent.putExtra("MainActAlamat", pengunjung.alamat)
        startActivity(intent)
    }

    private class ViewHolder(view: View?){
        val tvNama: TextView
        val tvJenis: TextView
        val tvalamat: TextView

        init {
            this.tvNama = view?.findViewById(R.id.tvNama) as TextView
            this.tvJenis = view?.findViewById(R.id.tvJenis) as TextView
            this.tvalamat = view?.findViewById(R.id.tvAlamat) as TextView
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
