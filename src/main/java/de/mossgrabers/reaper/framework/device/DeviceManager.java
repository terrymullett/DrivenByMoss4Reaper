// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2024
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.reaper.framework.device;

import de.mossgrabers.reaper.framework.IniFiles;
import de.mossgrabers.reaper.framework.daw.data.parameter.map.ParameterMap;
import de.mossgrabers.reaper.framework.daw.data.parameter.map.ParameterMapPage;
import de.mossgrabers.reaper.framework.daw.data.parameter.map.ParameterMapPageParameter;
import de.mossgrabers.reaper.ui.utils.LogModel;

import com.nikhaldimann.inieditor.IniEditor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Manages the information about all available devices.
 *
 * @author Jürgen Moßgraber
 */
public class DeviceManager
{
    private static final DeviceManager                INSTANCE             = new DeviceManager ();

    private static final String                       SECTION_FOLDERS      = "Folders";
    private static final Pattern                      PATTERN_NAME         = Pattern.compile ("(?<type>\\w+?)(?<instrument>i?):\\s*(?<name>(\\w|\\s)+)\\s*(\\((?<company>[^)]+)\\))?");
    private static final Set<String>                  NON_CATEGORIES       = Set.of ("ix", "till", "loser", "liteon", "sstillwell", "teej", "schwa", "u-he", "remaincalm_org");

    private static final Map<Integer, DeviceFileType> TYPE_CODES           = new HashMap<> (5);
    private static final Map<String, DeviceFileType>  DEVICE_FILE_TYPE_MAP = new HashMap<> ();
    private static final Map<String, String>          JS_CATEGORY_MAP      = new HashMap<> ();
    static
    {
        TYPE_CODES.put (Integer.valueOf (1), DeviceFileType.VST3);
        TYPE_CODES.put (Integer.valueOf (2), DeviceFileType.VST2);
        TYPE_CODES.put (Integer.valueOf (3), DeviceFileType.AU);
        TYPE_CODES.put (Integer.valueOf (4), DeviceFileType.LV2);
        TYPE_CODES.put (Integer.valueOf (5), DeviceFileType.CLAP);

        DEVICE_FILE_TYPE_MAP.put ("VST3", DeviceFileType.VST3);
        DEVICE_FILE_TYPE_MAP.put ("VST", DeviceFileType.VST2);
        DEVICE_FILE_TYPE_MAP.put ("AU", DeviceFileType.AU);
        DEVICE_FILE_TYPE_MAP.put ("LV2", DeviceFileType.LV2);
        DEVICE_FILE_TYPE_MAP.put ("CLAP", DeviceFileType.CLAP);
        DEVICE_FILE_TYPE_MAP.put ("JS", DeviceFileType.JS);

        JS_CATEGORY_MAP.put ("analysis", "Analysis");
        JS_CATEGORY_MAP.put ("delay", "Delay");
        JS_CATEGORY_MAP.put ("dynamics", "Dynamics");
        JS_CATEGORY_MAP.put ("filters", "Filter");
        JS_CATEGORY_MAP.put ("guitar", "Guitar");
        JS_CATEGORY_MAP.put ("loopsamplers", "Sampler");
        JS_CATEGORY_MAP.put ("meters", "Analysis");
        JS_CATEGORY_MAP.put ("midi", "MIDI");
        JS_CATEGORY_MAP.put ("misc", "Misc");
        JS_CATEGORY_MAP.put ("old_unsupported", "Misc");
        JS_CATEGORY_MAP.put ("pitch", "Pitch");
        JS_CATEGORY_MAP.put ("synthesis", "Synth");
        JS_CATEGORY_MAP.put ("utility", "Tools");
        JS_CATEGORY_MAP.put ("waveshapers", "Modulation");
    }

    private final List<DeviceMetadataImpl>  devices                = new ArrayList<> ();
    private final List<DeviceMetadataImpl>  instruments            = new ArrayList<> ();
    private final List<DeviceMetadataImpl>  effects                = new ArrayList<> ();
    private final Set<String>               categories             = new TreeSet<> ();
    private final Set<String>               vendors                = new TreeSet<> ();
    private final List<DeviceCollection>    collections            = new ArrayList<> ();
    private final Set<DeviceFileType>       availableFileTypes     = new TreeSet<> ();
    private final Set<DeviceArchitecture>   availableArchitectures = new TreeSet<> ();
    private final Map<String, ParameterMap> parameterMaps          = new HashMap<> ();
    private final List<DeviceFileType>      preferredTypes         = new ArrayList<> ();
    private IniFiles                        iniFiles;


    /**
     * Private due to singleton.
     */
    private DeviceManager ()
    {
        // Intentionally empty
    }


    /**
     * Get the singleton.
     *
     * @return The singleton
     */
    public static DeviceManager get ()
    {
        return INSTANCE;
    }


    /**
     * Get the number of all devices.
     *
     * @return The number
     */
    public int getNumDevices ()
    {
        synchronized (this.devices)
        {
            return this.devices.size ();
        }
    }


    /**
     * Get all available devices.
     *
     * @return The devices
     */
    public List<DeviceMetadataImpl> getAll ()
    {
        synchronized (this.devices)
        {
            return new ArrayList<> (this.devices);
        }
    }


    /**
     * Get all instrument devices.
     *
     * @return The instrument devices
     */
    public List<DeviceMetadataImpl> getInstruments ()
    {
        synchronized (this.devices)
        {
            return new ArrayList<> (this.instruments);
        }
    }


    /**
     * Get all effect devices.
     *
     * @return The effect devices
     */
    public List<DeviceMetadataImpl> getEffects ()
    {
        synchronized (this.devices)
        {
            return new ArrayList<> (this.effects);
        }
    }


    /**
     * Filter the devices by file type.
     *
     * @param deviceFileType Filter by device type (plugin format), may be null
     * @return The devices matching the filter criteria
     */
    public List<DeviceMetadataImpl> filterByFileType (final DeviceFileType deviceFileType)
    {
        return this.filterBy (deviceFileType, null, null, null, null, null);
    }


    /**
     * Filter the devices by category.
     *
     * @param category Filter by device category, may be null
     * @return The devices matching the filter criteria
     */
    public List<DeviceMetadataImpl> filterByCategory (final String category)
    {
        return this.filterBy (null, category, null, null, null, null);
    }


    /**
     * Filter the devices by a vendor.
     *
     * @param vendor Filter by device vendor, may be null
     * @return The devices matching the filter criteria
     */
    public List<DeviceMetadataImpl> filterByVendor (final String vendor)
    {
        return this.filterBy (null, null, vendor, null, null, null);
    }


    /**
     * Filter the devices by a collection.
     *
     * @param collection Filter by device collection, may be null
     * @return The devices matching the filter criteria
     */
    public List<DeviceMetadataImpl> filterByCollection (final DeviceCollection collection)
    {
        return this.filterBy (null, null, null, collection, null, null);
    }


    /**
     * Filter the devices by a location.
     *
     * @param location Filter by device location, may be null
     * @return The devices matching the filter criteria
     */
    public List<DeviceMetadataImpl> filterByLocation (final DeviceArchitecture location)
    {
        return this.filterBy (null, null, null, null, location, null);
    }


    /**
     * Filter the devices by type.
     *
     * @param deviceType Filter by device type, may be null
     * @return The devices matching the filter criteria
     */
    public List<DeviceMetadataImpl> filterByType (final DeviceType deviceType)
    {
        return this.filterBy (null, null, null, null, null, deviceType);
    }


    /**
     * Filter the devices by different criteria.
     *
     * @param fileType Filter by device type (plugin format), may be null
     * @param category Filter by device category, may be null
     * @param vendor Filter by device vendor, may be null
     * @param collection Filter by device collection, may be null
     * @param location Filter by device location, may be null
     * @param deviceType Filter by device type, may be null
     * @return The devices matching the filter criteria
     */
    public List<DeviceMetadataImpl> filterBy (final DeviceFileType fileType, final String category, final String vendor, final DeviceCollection collection, final DeviceArchitecture location, final DeviceType deviceType)
    {
        List<DeviceMetadataImpl> results = new ArrayList<> ();
        synchronized (this.devices)
        {
            for (final DeviceMetadataImpl d: this.devices)
            {
                if (accept (d, fileType, category, vendor, location, deviceType))
                    results.add (d);
            }
        }

        if (collection != null)
            results = collection.filter (results);
        return this.filterPreferredFileTypes (results);
    }


    /**
     * Filter by preferred types.
     *
     * @param results The devices to filter
     * @return The filtered list
     */
    private List<DeviceMetadataImpl> filterPreferredFileTypes (final List<DeviceMetadataImpl> results)
    {
        final List<DeviceFileType> prefTypes = this.getPreferredTypes ();
        if (prefTypes.isEmpty () || results.size () <= 1)
            return results;

        // Group identical devices (= with the same name) into a map
        final Map<String, Map<DeviceFileType, DeviceMetadataImpl>> identicalDevices = new TreeMap<> ();
        for (final DeviceMetadataImpl device: results)
        {
            final String deviceName = device.name ();
            identicalDevices.computeIfAbsent (deviceName, name -> new EnumMap<> (DeviceFileType.class)).put (device.getFileType (), device);
        }

        // Choose the preferred devices by their file type
        final List<DeviceMetadataImpl> resultsFiltered = new ArrayList<> ();
        for (final Map<DeviceFileType, DeviceMetadataImpl> deviceOptions: identicalDevices.values ())
        {
            // Find the device of the preferred type
            DeviceMetadataImpl deviceMetadata = null;
            for (final DeviceFileType prefType: prefTypes)
            {
                deviceMetadata = deviceOptions.get (prefType);
                if (deviceMetadata != null)
                    break;
            }

            // If no preferred type was found, add all
            if (deviceMetadata == null)
                resultsFiltered.addAll (deviceOptions.values ());
            else
                resultsFiltered.add (deviceMetadata);
        }
        return resultsFiltered;
    }


    private static boolean accept (final DeviceMetadataImpl d, final DeviceFileType fileType, final String category, final String vendor, final DeviceArchitecture architecture, final DeviceType deviceType)
    {
        if (fileType != null && d.getFileType () != fileType || category != null && !d.hasCategory (category))
            return false;

        if (vendor != null && !vendor.equals (d.getVendor ()) || architecture != null && d.getArchitecture () != architecture)
            return false;

        return deviceType == null || d.getType () == deviceType;
    }


    private List<DeviceFileType> getPreferredTypes ()
    {
        if (this.iniFiles == null)
            return Collections.emptyList ();

        final int pattern = this.iniFiles.getMainIniInteger ("REAPER-fxadd", "dupefilter", 0);

        // The pattern is a 16 bit integer consisting of four 4-bit values providing the potential
        // four preferred plugin types. Priority 1 is LSB. The values for the plugin types are:
        // Off: 0, VST3: 1, VST2: 2, AU: 3, LV2: 4, CLAP: 5

        this.preferredTypes.clear ();
        for (int i = 0; i < 4; i++)
        {
            final int code = pattern >> i * 4 & 15;
            final DeviceFileType type = TYPE_CODES.get (Integer.valueOf (code));
            if (type != null)
                this.preferredTypes.add (type);
        }

        return this.preferredTypes;
    }


    /**
     * Add a device to the manager.
     *
     * @param description The description line of the device, e.g. "VSTi: My Plugin (Company X)"
     * @param module The module of the plugin
     */
    public void addDeviceInfo (final String description, final String module)
    {
        final Matcher matcher = PATTERN_NAME.matcher (description);
        if (matcher.matches ())
        {
            final String type = matcher.group ("type");
            final String instrument = matcher.group ("instrument");
            final String deviceName = matcher.group ("name").trim ();
            String company = matcher.group ("company");

            final DeviceFileType fileType = DEVICE_FILE_TYPE_MAP.get (type);
            final DeviceType deviceType = instrument != null && "i".equals (instrument) ? DeviceType.INSTRUMENT : DeviceType.AUDIO_EFFECT;
            final DeviceArchitecture architecture;

            String category = null;
            switch (fileType)
            {
                case JS:
                    architecture = DeviceArchitecture.SCRIPT;

                    // Parse category or company from first part of module
                    final String [] modulePath = module.split ("/");
                    if (modulePath.length <= 1)
                        return;
                    if (NON_CATEGORIES.contains (modulePath[0].toLowerCase (Locale.US)))
                        company = modulePath[0];
                    else
                    {
                        final String mappedCategory = JS_CATEGORY_MAP.get (modulePath[0]);
                        category = mappedCategory == null ? modulePath[0] : mappedCategory;
                    }
                    break;

                default:
                    // TODO
                    architecture = DeviceArchitecture.X64;
                    break;
            }

            final DeviceMetadataImpl device = new DeviceMetadataImpl (deviceName, module, deviceType, fileType, architecture);
            if (company != null)
            {
                device.setVendor (company);
                this.vendors.add (company);
            }

            if (category != null)
            {
                device.setCategories (Collections.singleton (category));
                this.categories.add (category);
            }

            this.devices.add (device);
            if (deviceType == DeviceType.INSTRUMENT)
                this.instruments.add (device);
            else
                this.effects.add (device);

            this.availableFileTypes.add (fileType);
            this.availableArchitectures.add (architecture);
        }
    }


    /**
     * Load all information from some INI files in Reapers' configuration path.
     *
     * @param iniFiles Access to the INI files
     * @param logModel For logging
     */
    public void applyDeviceInfo (final IniFiles iniFiles, final LogModel logModel)
    {
        this.iniFiles = iniFiles;

        synchronized (this.devices)
        {
            // Load categories and vendor information
            final Set<String> categoriesSet = new TreeSet<> ();
            final Set<String> vendorsSet = new TreeSet<> ();
            if (iniFiles.isFxTagsPresent ())
                this.parseFXTagsFile (iniFiles.getIniFxTags (), categoriesSet, vendorsSet);

            this.categories.addAll (categoriesSet);
            this.vendors.addAll (vendorsSet);

            // Load collection filters
            if (iniFiles.isFxFoldersPresent ())
                this.parseCollectionFilters (iniFiles.getIniFxFolders ());

            // Load device maps
            if (iniFiles.isParamMapsPresent ())
                this.parseParameterMaps (iniFiles.getIniParamMaps ());

            // Set MIDI device type
            for (final DeviceMetadataImpl device: this.devices)
            {
                if (device.hasCategory ("MIDI"))
                    device.setType (DeviceType.MIDI_EFFECT);
            }

            // Finally sort the devices by their display name
            this.devices.sort ( (d1, d2) -> d1.getDisplayName ().compareToIgnoreCase (d2.getDisplayName ()));
        }
    }


    /**
     * Get the file types present on this system.
     *
     * @return The file types
     */
    public List<DeviceFileType> getAvailableFileTypes ()
    {
        return new ArrayList<> (this.availableFileTypes);
    }


    /**
     * Get the locations present on this system.
     *
     * @return The file types
     */
    public List<DeviceArchitecture> getAvailableLocations ()
    {
        return new ArrayList<> (this.availableArchitectures);
    }


    /**
     * Get all collected device categories.
     *
     * @return The set with all categories
     */
    public List<String> getCategories ()
    {
        synchronized (this.devices)
        {
            return new ArrayList<> (this.categories);
        }
    }


    /**
     * Get all collected device vendors.
     *
     * @return The set with all vendors
     */
    public List<String> getVendors ()
    {
        synchronized (this.devices)
        {
            return new ArrayList<> (this.vendors);
        }
    }


    /**
     * Get all configured collections.
     *
     * @return The collections
     */
    public List<DeviceCollection> getCollections ()
    {
        synchronized (this.devices)
        {
            return this.collections;
        }
    }


    /**
     * Get a collection by its' name.
     *
     * @param collectionName The name to look for
     * @return The collection or null if not found
     */
    public DeviceCollection getCollection (final String collectionName)
    {
        synchronized (this.devices)
        {
            for (final DeviceCollection collection: this.collections)
            {
                if (collection.getName ().equals (collectionName))
                    return collection;
            }
        }
        return null;
    }


    /**
     * Get the parameter mappings map. Will be modified outside of class, which is intentional.
     *
     * @return The map
     */
    public Map<String, ParameterMap> getParameterMaps ()
    {
        return this.parameterMaps;
    }


    /**
     * Parses the FX tags file.
     *
     * @param iniFile The INI file from which to parse
     * @param categoriesSet The categories set
     * @param vendorsSet The vendors set
     */
    private void parseFXTagsFile (final IniEditor iniFile, final Set<String> categoriesSet, final Set<String> vendorsSet)
    {
        for (final DeviceMetadataImpl d: this.devices)
        {
            final String categoriesStr = iniFile.get ("category", d.getModule ());
            if (categoriesStr != null)
            {
                final Set<String> cats = new HashSet<> ();
                for (final String category: categoriesStr.split ("\\|"))
                {
                    if (!NON_CATEGORIES.contains (category))
                        cats.add (category);
                }
                if (cats.isEmpty ())
                {
                    if (d.getType () == DeviceType.INSTRUMENT)
                        cats.add ("Synth");
                    else if (d.getType () == DeviceType.MIDI_EFFECT)
                        cats.add ("MIDI");
                }
                d.setCategories (cats);
                categoriesSet.addAll (cats);
            }

            final String vendor = iniFile.get ("developer", d.getModule ());
            if (vendor == null)
                continue;
            d.setVendor (vendor);
            vendorsSet.add (vendor);
        }
    }


    /**
     * Parses the collection filter file.
     *
     * @param iniFile The INI file from which to parse
     */
    private void parseCollectionFilters (final IniEditor iniFile)
    {
        for (int i = 0; i < getInt (iniFile, SECTION_FOLDERS, "NbFolders", 0); i++)
        {
            final int id = getInt (iniFile, SECTION_FOLDERS, "Id" + i, -1);
            if (id >= 0)
            {
                final String collectionName = iniFile.get (SECTION_FOLDERS, "Name" + i);
                if (collectionName != null)
                {
                    final DeviceCollection deviceCollection = new DeviceCollection (collectionName);
                    this.collections.add (deviceCollection);

                    final String collectionSection = "Folder" + id;
                    for (int j = 0; j < getInt (iniFile, collectionSection, "Nb", 0); j++)
                    {
                        final String item = iniFile.get (collectionSection, "Item" + j);
                        if (item == null)
                            continue;
                        final int type = getInt (iniFile, collectionSection, "Type" + j, 0);
                        deviceCollection.addItem (item, type);
                    }
                }
            }
        }
    }


    /**
     * Parses the parameter maps configuration file.
     *
     * @param iniFile The INI file from which to parse
     */
    private void parseParameterMaps (final IniEditor iniFile)
    {
        for (final String name: iniFile.sectionNames ())
        {
            final Map<String, String> sectionMap = iniFile.getSectionMap (name);
            final ParameterMap parameterMap = new ParameterMap (name);
            final List<ParameterMapPage> pages = parameterMap.getPages ();

            int i = 0;
            String pageName;
            while ((pageName = sectionMap.get ("page" + i)) != null)
            {
                final ParameterMapPage page = new ParameterMapPage (pageName);

                final String params = sectionMap.get ("params" + i);
                if (params == null)
                    break;

                final String [] parts = params.split (",");
                if (parts.length != 16)
                    break;

                final List<ParameterMapPageParameter> parameters = page.getParameters ();
                for (int p = 0; p < 16; p += 2)
                    parameters.get (p / 2).assign (Integer.parseInt (parts[p]), parts[p + 1]);

                pages.add (page);

                i++;
            }

            this.parameterMaps.put (name, parameterMap);
        }
    }


    /**
     * Get a value from the INI file and convert it to an integer.
     *
     * @param iniFile The INI file
     * @param category The category section
     * @param name The name of the entry
     * @param defaultValue The default value
     * @return The integer value
     */
    private static int getInt (final IniEditor iniFile, final String category, final String name, final int defaultValue)
    {
        final String value = iniFile.get (category, name);
        try
        {
            return value == null ? defaultValue : Integer.parseInt (value);
        }
        catch (final NumberFormatException ex)
        {
            return defaultValue;
        }
    }
}
