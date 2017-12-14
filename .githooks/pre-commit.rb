`git status --porcelain`.lines do |line|
    changed_file = line.split(' ', 2)[1].strip()
    if (File.extname(changed_file).downcase() == '.java')
        if (system("google-java-format --aosp --replace \"#{changed_file}\"") == nil)
            puts "Cannot find google-java-format in path"
            exit(1)
        end
        system "git add \"#{changed_file}\""
    end
end
